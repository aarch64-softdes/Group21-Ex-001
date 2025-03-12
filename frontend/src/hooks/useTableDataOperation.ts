import { SortConfig } from "@/components/table/TableSort";
import { FilterOption, FilterParams } from "@/types/filter";
import { Column, QueryHook, TableActions } from "@/types/table";
import { useCallback, useEffect, useState } from "react";

export const useTableAdd = <T extends { id: string }>(
  actions?: TableActions
) => {
  const [isAdding, setIsAdding] = useState(false);
  const [dialogOpen, setDialogOpen] = useState(false);

  const handleAdd = async (value: Partial<T>) => {
    try {
      setIsAdding(true);
      setDialogOpen(false);
      await actions?.onAdd?.(value);
    } catch (error) {
      console.error(error);
    } finally {
      setIsAdding(false);
    }
  };

  const handleShowDialog = () => {
    setDialogOpen(true);
  };

  return { isAdding, dialogOpen, setDialogOpen, handleAdd, handleShowDialog };
};

export const useTableEdit = <T extends { id: string }>(
  initialData: T[],
  columns: Column<T>[],
  actions?: TableActions
) => {
  const [editingRow, setEditingRow] = useState<string | null>(null);
  const [editedValues, setEditedValues] = useState<T | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [isSaving, setIsSaving] = useState(false);

  const validateFields = () => {
    const newErrors: Record<string, string> = {};
    columns.forEach((column) => {
      if (column.editable && column.validate) {
        const error = column.validate(editedValues?.[column.key] as T[keyof T]);
        if (error) {
          newErrors[String(column.key)] = error;
        }
      }
    });
    setFieldErrors(newErrors);
    return newErrors;
  };

  const handleEdit = (id: string) => {
    setEditingRow(id);
    const currentRow = initialData.find((row) => row.id === id);
    if (currentRow) {
      setEditedValues(currentRow);
    }
  };

  const handleCancel = () => {
    setEditingRow(null);
    setEditedValues(null);
    setFieldErrors({});
  };

  const handleSave = async (id: string) => {
    try {
      setIsSaving(true);
      const errors = validateFields();
      if (Object.keys(errors).length > 0) {
        console.error("Validation failed", errors);
        return;
      }

      await actions?.onSave?.(id, editedValues as T);
      handleCancel();
    } catch (error) {
      console.error(error);
    } finally {
      setIsSaving(false);
    }
  };

  const handleCellValueChange = (key: keyof T, value: string) => {
    if (editedValues) {
      setEditedValues({
        ...editedValues,
        [key]: value,
      });
      // Clear error when user starts typing
      if (fieldErrors[String(key)]) {
        setFieldErrors((prev) => ({ ...prev, [String(key)]: "" }));
      }
    }
  };

  return {
    editingRow,
    editedValues,
    fieldErrors,
    isSaving,
    handleEdit,
    handleCancel,
    handleSave,
    handleCellValueChange,
  };
};

export const useTableDelete = (actions?: TableActions) => {
  const [isDeleting, setIsDeleting] = useState(false);
  const [deletingRow, setDeletingRow] = useState<string | null>(null);

  const handleDelete = async (id: string) => {
    try {
      setIsDeleting(true);
      setDeletingRow(id);
      await actions?.onDelete?.(id);
      setDeletingRow(null);
    } catch (error) {
      console.error(error);
    } finally {
      setIsDeleting(false);
    }
  };

  return {
    isDeleting,
    deletingRow,
    handleDelete,
  };
};

export function useGenericTableData<T>({
  useQueryHook,
  filterOptions,
  defaultSortColumn,
}: {
  useQueryHook: QueryHook<T>;
  filterOptions: FilterOption[];
  defaultSortColumn?: string;
}) {
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [sortConfig, setSortConfig] = useState<SortConfig>({
    key: defaultSortColumn ?? null,
    direction: "asc",
  });

  const [filters, setFilters] = useState<Record<string, FilterParams>>(
    filterOptions.reduce((acc, option) => {
      switch (option.type) {
        case "search":
          acc[option.id] = option.value ?? "";
          break;

        default:
          break;
      }

      return acc;
    }, {} as Record<string, FilterParams>)
  );

  const handleSort = useCallback(
    (key: string, forcedDirection?: "asc" | "desc" | null) => {
      if (forcedDirection) {
        setSortConfig({ key, direction: forcedDirection });
      } else {
        setSortConfig({ key: defaultSortColumn ?? null, direction: "asc" });
      }
    },
    []
  );

  const query = useQueryHook({
    page,
    pageSize,
    filters: filters,
    sort: sortConfig,
  });

  useEffect(() => {
    if (query.data?.data.length === 0 && page > 1) {
      setPage(1);
    }
  }, [query.data?.data.length, page]);

  return {
    data: query.data?.data ?? [],
    pagination: {
      currentPage: page,
      totalPages: query.data?.totalPages ?? 0,
      totalItems: query.data?.totalItems ?? 0,
      pageSize,
      onPageChange: setPage,
      onPageSizeChange: setPageSize,
    },
    state: {
      isLoading: query.isLoading,
      isError: query.isError,
      isFetching: query.isFetching,
      error: query.error,
    },
    sort: {
      sortConfig: sortConfig,
      onSort: (key: string, direction: "asc" | "desc" | null) => {
        handleSort(key, direction);
        setPage(1);
      },
    },
    filters: {
      value: filters,
      onChange: (key: string, value: FilterParams) => {
        setFilters((prev) => ({ ...prev, [key]: value }));
        setPage(1);
      },
    },
  };
}
