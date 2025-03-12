import { Dialog, DialogContent } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

import { cn } from "@/lib/utils";
import ActionCell from "./ActionCell";
import SkeletonTable from "./SkeletonTable";

import TablePagination from "@/components/table/TablePagination";
import {
  useGenericTableData,
  useTableAdd,
  useTableDelete,
  useTableEdit,
} from "@/hooks/useTableDataOperation";
import { GenericTableProps } from "@/types/table";
import { PlusCircle } from "lucide-react";
import { useMemo } from "react";
import SearchFilter from "@/components/filter/SearchFilter";
import { Accordion } from "@/components/ui/accordion";
import LoadingButton from "@/components/ui/loadingButton";
import { Separator } from "@/components/ui/separator";
import TableSort from "./TableSort";

const GenericTable = <T extends { id: string }>({
  tableTitle,
  addingTitle,
  columns,
  actions,
  formComponent: CreateForm,
  disabledActions = {},
  queryHook,
  filterOptions,
  requireDeleteConfirmation,
  additionalActions = [],
}: GenericTableProps<T>) => {
  const defaultSortColumn = columns.find(
    (column) => column.isDefaultSort
  )?.header;

  const { data, pagination, state, sort, filters } = useGenericTableData({
    useQueryHook: queryHook,
    filterOptions,
    defaultSortColumn,
  });

  const {
    editingRow,
    editedValues,
    fieldErrors,
    isSaving,
    handleEdit,
    handleCancel,
    handleSave,
    handleCellValueChange,
  } = useTableEdit(data, columns, actions);
  const { isDeleting, deletingRow, handleDelete } = useTableDelete(actions);
  const { isAdding, dialogOpen, setDialogOpen, handleAdd, handleShowDialog } =
    useTableAdd(actions);

  const tableBody = useMemo(() => {
    if (state.isFetching) {
      return <SkeletonTable rows={pagination.pageSize} variant="body" />;
    }

    if (data.length === 0 || state.isError) {
      if (state.isError) {
        throw state.error;
      }

      return (
        <TableBody>
          <TableRow>
            <TableCell
              className="text-center text-red-500"
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
          <TableRow className="p-0" key={cell.id}>
            {columns.map((column) => (
              <TableCell
                key={column.key.toString()}
                className="py-1"
                style={{
                  minWidth: column.style?.minWidth,
                  maxWidth: column.style?.maxWidth,
                  width: column.style?.width,
                }}
              >
                {editingRow === cell.id && column.editable ? (
                  <Input
                    value={String(
                      editedValues?.[column.key] ?? cell[column.key]
                    )}
                    onChange={(e) =>
                      handleCellValueChange(column.key, e.target.value)
                    }
                    className={cn(
                      "h-full w-full border-2 p-1 focus:border-slate-800 focus-visible:ring-transparent",
                      fieldErrors[String(column.key)] ? "border-red-500" : ""
                    )}
                  />
                ) : (
                  String(cell[column.key])
                )}
              </TableCell>
            ))}
            <TableCell className="min-w-40 py-1">
              <ActionCell
                requireDeleteConfirmation={requireDeleteConfirmation}
                isEditing={editingRow === cell.id}
                isDeleting={deletingRow === cell.id && isDeleting}
                isSaving={isSaving}
                onEdit={() => handleEdit(cell.id)}
                onDelete={() => handleDelete(cell.id)}
                onSave={() => handleSave(cell.id)}
                onCancel={handleCancel}
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
    editingRow,
    editedValues,
    fieldErrors,
    deletingRow,
    isDeleting,
    isSaving,
    pagination.pageSize,
    handleCellValueChange,
    handleEdit,
    handleDelete,
    handleSave,
    handleCancel,
    disabledActions,
  ]);

  const tableHeader = useMemo(
    () => (
      <TableHeader>
        <TableRow>
          {columns.map((column) => (
            <TableHead key={column.header} className="text-blue-500">
              <TableSort
                columnKey={String(column.key)}
                columnHeader={column.header}
                sortConfig={sort.sortConfig}
                onSort={sort.onSort}
                sortable={column.sortable}
              />
            </TableHead>
          ))}
          <TableHead className="w-4 text-blue-500">Action</TableHead>
        </TableRow>
      </TableHeader>
    ),
    [columns, sort.sortConfig, sort.onSort]
  );

  const renderFilters = useMemo(
    () =>
      filterOptions.map((filterOption) => {
        switch (filterOption.type) {
          // case "enum":
          //   return (
          //     <EnumFilter
          //       key={filterOption.id}
          //       onChange={(value) => filters.onChange(filterOption.id, value)}
          //       {...filterOption}
          //       componentType="accordion"
          //     />
          //   );

          // case "range":
          //   return (
          //     <RangeFilter
          //       key={filterOption.id}
          //       value={filters.value[filterOption.id] as [number, number]}
          //       onChange={(value) => filters.onChange(filterOption.id, value)}
          //       {...filterOption}
          //       componentType="accordion"
          //     />
          //   );

          // case "date":
          //   return (
          //     <DateRangeFilter
          //       key={filterOption.id}
          //       value={filters.value[filterOption.id] as [Date, Date]}
          //       onChange={(value) => filters.onChange(filterOption.id, value)}
          //       {...filterOption}
          //       componentType="accordion"
          //     />
          //   );

          case "search":
            return (
              <SearchFilter
                key={filterOption.id}
                onChange={(value) => filters.onChange(filterOption.id, value)}
                {...filterOption}
                componentType="accordion"
              />
            );

          default:
            return null;
        }
      }),
    [filterOptions, filters.onChange, filters.value]
  );

  if (state.isLoading) {
    return <SkeletonTable rows={10} />;
  }

  return (
    <>
      <div className="flex h-full min-w-1/5 flex-col gap-4 rounded-md border-2 px-4">
        <h2 className="pt-4 text-center text-2xl font-semibold">
          {tableTitle}
        </h2>

        <LoadingButton
          variant="outline"
          className="w-full items-center gap-2"
          onClick={handleShowDialog}
          isLoading={isAdding}
        >
          <PlusCircle className="h-5 w-5" />
          {addingTitle}
        </LoadingButton>
        <Separator />

        {filterOptions.length > 0 && (
          <>
            <p className="font-semibold text-gray-400">Filters</p>
            <Accordion type="multiple" className="flex w-full flex-col gap-2">
              {renderFilters}
            </Accordion>
          </>
        )}
      </div>

      <div className="flex-grow">
        <Table>
          {tableHeader}

          {tableBody}
        </Table>

        <Separator />
        <TablePagination {...pagination} />
      </div>

      <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
        <DialogContent>
          <CreateForm onSubmit={handleAdd} />
        </DialogContent>
      </Dialog>
    </>
  );
};

export default GenericTable;
