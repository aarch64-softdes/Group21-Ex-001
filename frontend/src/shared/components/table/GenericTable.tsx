import { Dialog, DialogContent } from '@/components/ui/dialog';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';

import ActionCell from './ActionCell';
import SkeletonTable from './SkeletonTable';

import SearchFilter from '@/components/filter/SearchFilter';
import TablePagination from '@/components/table/TablePagination';
import LoadingButton from '@/components/ui/loadingButton';
import { Separator } from '@/components/ui/separator';
import {
  useGenericTableData,
  useTableAdd,
  useTableDelete,
  useTableEdit,
} from '@/shared/hooks/useTableDataOperation';
import { GenericTableProps } from '@/core/types/table';
import { PlusCircle } from 'lucide-react';
import { useMemo, useState } from 'react';
import TableSort from './TableSort';
import FileImportButton from './FileImportButton';
import FileExportButton from './FileExportButton';

const GenericTable = <T extends { id: string }>({
  tableTitle,
  addingTitle,
  columns,
  actions,
  formComponent: FormComponent,
  detailComponent: DetailComponent,
  disabledActions = {},
  queryHook,
  filterOptions,
  requireDeleteConfirmation,
  additionalActions = [],
  disablePagination = false,
  fileOptions,
}: GenericTableProps<T>) => {
  const defaultSortColumn = columns.find(
    (column) => column.isDefaultSort,
  )?.header;

  const { data, pagination, state, sort, filters } = useGenericTableData({
    useQueryHook: queryHook,
    filterOptions,
    defaultSortColumn,
  });

  // State for detail dialog
  const [detailDialogOpen, setDetailDialogOpen] = useState(false);
  const [currentDetailItem, setCurrentDetailItem] = useState<T | null>(null);

  // Use the edit hook instead of managing state directly
  const {
    editDialogOpen,
    setEditDialogOpen,
    currentEditItem,
    isEditSaving,
    handleEditClick,
    handleEditSave,
  } = useTableEdit<T>(actions);

  const { isDeleting, deletingRow, handleDelete } = useTableDelete(actions);
  const { isAdding, dialogOpen, setDialogOpen, handleAdd, handleShowDialog } =
    useTableAdd(actions);

  const tableBody = useMemo(() => {
    if (state.isFetching) {
      return <SkeletonTable rows={pagination.pageSize} variant='body' />;
    }

    if (data.length === 0 || state.isError) {
      if (state.isError) {
        return (
          <TableBody>
            <TableRow>
              <TableCell
                className='text-center text-red-500'
                colSpan={columns.length + 1}
              >
                Error fetching data
              </TableCell>
            </TableRow>
          </TableBody>
        );
      }

      return (
        <TableBody>
          <TableRow>
            <TableCell
              className='text-center text-red-500'
              colSpan={columns.length + 1}
            >
              No data found
            </TableCell>
          </TableRow>
        </TableBody>
      );
    }

    return (
      <TableBody>
        {data.map((cell: T) => (
          <TableRow className='p-0' key={cell.id}>
            {columns.map((column) => (
              <TableCell
                key={column.key.toString()}
                className='py-1'
                style={{
                  minWidth: column.style?.minWidth,
                  maxWidth: column.style?.maxWidth,
                  width: column.style?.width,
                }}
              >
                {column.transform
                  ? column.transform(cell[column.key])
                  : String(cell[column.key])}
              </TableCell>
            ))}
            <TableCell className='min-w-20 py-1'>
              <ActionCell
                requireDeleteConfirmation={requireDeleteConfirmation}
                isDeleting={deletingRow === cell.id && isDeleting}
                onView={() => {
                  setCurrentDetailItem(cell);
                  setDetailDialogOpen(true);
                }}
                onEdit={() => handleEditClick(cell.id, data)}
                onDelete={() => handleDelete(cell.id)}
                disabledActions={disabledActions}
                additionalActions={additionalActions.map((action) => ({
                  label: action.label,
                  handler: () => action.handler(cell.id),
                }))}
              />
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    );
  }, [
    data,
    state.isFetching,
    state.isError,
    columns,
    deletingRow,
    isDeleting,
    pagination.pageSize,
    handleDelete,
    handleEditClick,
    disabledActions,
  ]);

  const tableHeader = useMemo(
    () => (
      <TableHeader>
        <TableRow>
          {columns.map((column) => (
            <TableHead key={column.header} className='text-blue-500'>
              <TableSort
                columnKey={String(column.key)}
                columnHeader={column.header}
                sortConfig={sort.sortConfig}
                onSort={sort.onSort}
                sortable={column.sortable}
              />
            </TableHead>
          ))}
          <TableHead className='w-4 text-blue-500'>Action</TableHead>
        </TableRow>
      </TableHeader>
    ),
    [columns, sort.sortConfig, sort.onSort],
  );

  const renderFilters = useMemo(
    () =>
      filterOptions.map((filterOption) => {
        switch (filterOption.type) {
          case 'search':
            return (
              <SearchFilter
                key={filterOption.id}
                onChange={(value) => filters.onChange(filterOption.id, value)}
                {...filterOption}
                value={filters.value[filterOption.id] || ''}
                componentType='popover'
              />
            );

          default:
            return null;
        }
      }),
    [filterOptions, filters.onChange, filters.value],
  );

  return (
    <div className='flex flex-col gap-4 w-full'>
      {/* Table heading and actions */}
      <div className='flex flex-col gap-2'>
        <h2 className='text-2xl font-semibold text-center'>{tableTitle}</h2>

        <div className='flex justify-between items-center'>
          <div className='flex items-center gap-2'>{renderFilters}</div>
          <div className='flex items-center gap-2'>
            {fileOptions?.enableImport && fileOptions.onImport && (
              <FileImportButton
                onImport={fileOptions.onImport}
                disabled={state.isLoading || state.isFetching}
              />
            )}

            {fileOptions?.enableExport && fileOptions.onExport && (
              <FileExportButton
                onExport={fileOptions.onExport}
                disabled={state.isLoading || data.length === 0}
              />
            )}

            <LoadingButton
              variant='outline'
              className='flex items-center gap-2'
              onClick={handleShowDialog}
              isLoading={isAdding}
            >
              <PlusCircle className='h-5 w-5' />
              {addingTitle}
            </LoadingButton>
          </div>
        </div>
      </div>

      {/* Table content */}
      <div className='border rounded-md'>
        {state.isLoading ? (
          <SkeletonTable rows={pagination.pageSize} columns={columns.length} />
        ) : (
          <div className='overflow-x-auto'>
            <Table>
              {tableHeader}
              {tableBody}
            </Table>

            {/* Conditionally render pagination */}
            {!disablePagination && (
              <>
                <Separator />
                <TablePagination {...pagination} />
              </>
            )}
          </div>
        )}
      </div>

      {/* Add Dialog */}
      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent className='max-w-screen w-[90%] h-[97%] p-4'>
          <FormComponent
            onSubmit={handleAdd}
            onCancel={() => setDialogOpen(false)}
          />
        </DialogContent>
      </Dialog>

      {/* Edit Dialog */}
      <Dialog open={editDialogOpen} onOpenChange={setEditDialogOpen}>
        <DialogContent className='max-w-screen w-[90%] h-[97%] p-4'>
          <FormComponent
            id={currentEditItem?.id}
            onSubmit={handleEditSave}
            onCancel={() => setEditDialogOpen(false)}
            isLoading={isEditSaving}
            isEditing={true}
          />
        </DialogContent>
      </Dialog>

      {/* Detail Dialog */}
      <Dialog open={detailDialogOpen} onOpenChange={setDetailDialogOpen}>
        <DialogContent className='max-w-screen w-[90%] h-[97%] p-4'>
          <DetailComponent id={currentDetailItem?.id} />
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default GenericTable;
