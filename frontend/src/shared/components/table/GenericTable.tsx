import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@ui/dialog';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@ui/table';

import ActionCell from './ActionCell';
import SkeletonTable from './SkeletonTable';

import SearchFilter from '@components/filter/SearchFilter';
import TablePagination from '@components/table/TablePagination';
import LoadingButton from '@components/ui/loadingButton';
import { Separator } from '@components/ui/separator';
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
import { getNestedValue } from '@/shared/lib/utils';
import { ScrollArea } from '../ui/scroll-area';
import React from 'react';

const GenericTable = <T extends { id: string }>({
  tableTitle,
  addAction = {
    onAdd: () => {},
    disabled: false,
    title: 'Add',
  },
  columns,
  queryHook,
  filterOptions,
  disablePagination = false,
  tableOptions,
  actionCellProperties = {
    requireDeleteConfirmation: false,
    edit: {
      onSave: () => {},
      disabled: false,
    },
    delete: {
      onDelete: () => {},
      disabled: false,
    },
    additionalActions: [],
  },
  customActionCellComponent = undefined,
  emptyMessage = 'No data available',
  metadata = {},
}: GenericTableProps<T>) => {
  const disabledActions = {
    edit: actionCellProperties.edit.disabled,
    delete: actionCellProperties.delete.disabled,
  };

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
  } = useTableEdit<T>(actionCellProperties?.edit.onSave);

  const { isDeleting, deletingRow, handleDelete } = useTableDelete(
    actionCellProperties?.delete.onDelete,
  );
  const { isAdding, dialogOpen, setDialogOpen, handleAdd, handleShowDialog } =
    useTableAdd(addAction?.onAdd);

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
    }

    return (
      <TableBody>
        {data.map((cell: T) => (
          <TableRow className='p-0' key={cell.id}>
            {columns.map((column) => (
              <TableCell
                key={column.key.toString()}
                className='py-1 h-10'
                style={{
                  minWidth: column.style?.minWidth,
                  maxWidth: column.style?.maxWidth,
                  width: column.style?.width,
                  textAlign: column.style?.textAlign,
                }}
              >
                {(() => {
                  // Get the value using the path or key
                  const value = column.nested
                    ? getNestedValue(cell, column.key.toString())
                    : cell[column.key as keyof typeof cell];

                  // Apply transform if specified or convert to string
                  return column.transform
                    ? column.transform(value, cell)
                    : value !== null && value !== undefined
                    ? String(value)
                    : '';
                })()}
              </TableCell>
            ))}
            <TableCell className='min-w-20 py-1 flex justify-center items-center'>
              {customActionCellComponent ? (
                React.createElement(customActionCellComponent, {
                  ...cell,
                  ...metadata,
                })
              ) : (
                <ActionCell
                  requireDeleteConfirmation={
                    actionCellProperties.requireDeleteConfirmation
                  }
                  isDeleting={deletingRow === cell.id && isDeleting}
                  onView={() => {
                    setCurrentDetailItem(cell);
                    setDetailDialogOpen(true);
                  }}
                  onEdit={() => handleEditClick(cell.id, data)}
                  onDelete={() => handleDelete(cell.id)}
                  disabledActions={disabledActions}
                  additionalActions={actionCellProperties.additionalActions?.map(
                    (action) => ({
                      label: action.label,
                      handler: () => action.handler(cell.id),
                      disabled: action.disabled ? action.disabled(cell) : false,
                    }),
                  )}
                />
              )}
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
            <TableHead
              key={column.header}
              className='text-blue-500'
              style={{
                minWidth: column.style?.minWidth,
                maxWidth: column.style?.maxWidth,
                width: column.style?.width,
                textAlign: column.style?.textAlign,
              }}
            >
              <TableSort
                columnKey={String(column.key)}
                columnHeader={column.header}
                sortConfig={sort.sortConfig}
                onSort={sort.onSort}
                sortable={column.sortable}
              />
            </TableHead>
          ))}
          <TableHead className='w-16 text-blue-500 text-center'>
            Action
          </TableHead>
          <TableHead className='w-4' />
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
                value={filters.value[filterOption.id] as string}
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
            {tableOptions?.map((option, index) => (
              <React.Fragment key={`table-option-${index}`}>
                {React.cloneElement(option, {
                  disabled: state.isLoading || state.isFetching,
                })}
              </React.Fragment>
            ))}

            {addAction && !addAction.disabled && (
              <LoadingButton
                variant='outline'
                className='flex items-center gap-2'
                onClick={handleShowDialog}
                isLoading={isAdding}
              >
                <PlusCircle className='h-5 w-5' />
                {addAction.title}
              </LoadingButton>
            )}
          </div>
        </div>
      </div>

      {/* Table content */}
      <div className='border rounded-md'>
        {data.length === 0 && !state.isLoading ? (
          <div className='flex items-center justify-center h-24'>
            <p className='text-muted-foreground'>{emptyMessage}</p>
          </div>
        ) : state.isLoading ? (
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
        <DialogContent className='max-w-screen w-[90%] p-4 rounded-md max-h-[96vh] flex flex-col'>
          <DialogHeader>
            <DialogTitle />
          </DialogHeader>
          <div className='flex-1 overflow-hidden my-4'>
            <ScrollArea className='h-[calc(90vh-50px)] w-full'>
              {actionCellProperties.formComponent && (
                <actionCellProperties.formComponent
                  onSubmit={handleAdd}
                  onCancel={() => setDialogOpen(false)}
                />
              )}
            </ScrollArea>
          </div>
        </DialogContent>
      </Dialog>

      {/* Edit Dialog */}
      <Dialog open={editDialogOpen} onOpenChange={setEditDialogOpen}>
        <DialogContent className='max-w-screen w-[90%] p-4 rounded-md max-h-[96vh] flex flex-col'>
          <DialogHeader>
            <DialogTitle />
          </DialogHeader>
          <div className='flex-1 overflow-hidden my-4'>
            <ScrollArea className='h-[calc(90vh-50px)] w-full'>
              {actionCellProperties.formComponent && (
                <actionCellProperties.formComponent
                  id={currentEditItem?.id}
                  onSubmit={handleEditSave}
                  onCancel={() => setEditDialogOpen(false)}
                  isLoading={isEditSaving}
                  isEditing={true}
                />
              )}
            </ScrollArea>
          </div>
        </DialogContent>
      </Dialog>

      {/* Detail Dialog */}
      <Dialog open={detailDialogOpen} onOpenChange={setDetailDialogOpen}>
        <DialogContent className='max-w-screen w-[90%] p-4 rounded-md max-h-[96vh] flex flex-col'>
          <DialogHeader>
            <DialogTitle />
          </DialogHeader>
          <div className='flex-1 overflow-hidden my-4'>
            <ScrollArea className='h-[calc(90vh-50px)] w-full'>
              {actionCellProperties.detailComponent && (
                <actionCellProperties.detailComponent
                  id={currentDetailItem?.id}
                />
              )}
            </ScrollArea>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default GenericTable;
