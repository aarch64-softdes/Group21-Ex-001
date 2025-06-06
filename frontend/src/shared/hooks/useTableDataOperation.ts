import { FilterOption, FilterParams } from '@/core/types/filter';
import {
  QueryHook,
  SortConfig,
  SaveAction,
  DeleteAction,
  AddAction,
} from '@/core/types/table';
import { useCallback, useEffect, useState } from 'react';

export const useTableAdd = <T extends { id: string }>(onAdd: AddAction) => {
  const [isAdding, setIsAdding] = useState(false);
  const [dialogOpen, setDialogOpen] = useState(false);

  const handleAdd = async (value: Partial<T>) => {
    try {
      setIsAdding(true);
      await onAdd?.(value);
      setDialogOpen(false);
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

export const useTableEdit = <T extends { id: string }>(onSave: SaveAction) => {
  const [editDialogOpen, setEditDialogOpen] = useState(false);
  const [currentEditItem, setCurrentEditItem] = useState<T | null>(null);
  const [isEditSaving, setIsEditSaving] = useState(false);

  // Handle edit button click
  const handleEditClick = useCallback((id: string, data: T[]) => {
    const itemToEdit = data.find((item) => item.id === id);
    if (itemToEdit) {
      setCurrentEditItem(itemToEdit);
      setEditDialogOpen(true);
    }
  }, []);

  // Handle save after editing
  const handleEditSave = async (updatedData: Partial<T>) => {
    if (!currentEditItem) return;

    try {
      setIsEditSaving(true);
      await onSave(currentEditItem.id, updatedData as T);
      setEditDialogOpen(false);
      setCurrentEditItem(null);
    } catch (error) {
      console.error('Error editing:', error);
    } finally {
      setIsEditSaving(false);
    }
  };

  return {
    editDialogOpen,
    setEditDialogOpen,
    currentEditItem,
    setCurrentEditItem,
    isEditSaving,
    handleEditClick,
    handleEditSave,
  };
};

export const useTableDelete = (onDelete: DeleteAction) => {
  const [isDeleting, setIsDeleting] = useState(false);
  const [deletingRow, setDeletingRow] = useState<string | null>(null);

  const handleDelete = async (id: string) => {
    try {
      setIsDeleting(true);
      setDeletingRow(id);
      await onDelete?.(id);
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
    direction: 'asc',
  });

  const [filters, setFilters] = useState<Record<string, FilterParams>>(
    filterOptions.reduce((acc, option) => {
      switch (option.type) {
        case 'search':
          acc[option.id] = option.value ?? '';
          break;

        default:
          break;
      }

      return acc;
    }, {} as Record<string, FilterParams>),
  );

  const handleSort = useCallback(
    (key: string, forcedDirection?: 'asc' | 'desc' | null) => {
      if (forcedDirection) {
        setSortConfig({ key, direction: forcedDirection });
      } else {
        setSortConfig({ key: defaultSortColumn ?? null, direction: 'asc' });
      }
    },
    [],
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
      onSort: (key: string, direction: 'asc' | 'desc' | null) => {
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
    refetch: () => {
      query.refetch();
    },
  };
}
