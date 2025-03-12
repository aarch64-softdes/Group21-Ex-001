import { SortConfig } from "@/components/table/TableSort";
import { UseQueryResult } from "@tanstack/react-query";
import { FilterOption, FilterParams } from "./filter";
import { ApiResponse } from "./apiResponse";

export type ColumnStyle = {
  width?: string;
  minWidth?: string;
  maxWidth?: string;
};

export interface Column<T> {
  header: string;
  key: keyof T;
  editable?: boolean;
  isDefaultSort?: boolean;
  sortable?: boolean;
  style?: ColumnStyle;
  validate?: (value: T[keyof T]) => string | null;
}

// Must use "any" here because the type of the data is not known
export interface TableActions {
  onSave?: (id: string, updatedData: any) => void | Promise<void>;
  onDelete?: (id: string) => void | Promise<void>;
  onAdd?: (data: any) => void | Promise<void>;
}

export interface TablePaginationProps {
  currentPage: number;
  totalPages: number;
  totalItems: number;
  pageSize: number;
  onPageChange: (page: number) => void;
  onPageSizeChange?: (pageSize: number) => void;
}

export interface AdditionalAction {
  label: string;
  handler: (id: string) => void;
}

export interface ActionCellProps {
  requireDeleteConfirmation?: boolean;
  isEditing?: boolean;
  isDeleting?: boolean;
  isSaving?: boolean;
  onEdit?: () => void;
  onDelete?: () => void;
  onSave?: () => void;
  onCancel?: () => void;
  disabledActions?: {
    edit?: boolean;
    delete?: boolean;
  };
  additionalActions?: AdditionalAction[];
}

export type QueryHook<T> = (
  data: QueryHookParams
) => UseQueryResult<ApiResponse<T>>;

export type QueryHookParams = {
  page: number;
  pageSize: number;
  filters: Record<string, FilterParams>;
  sort: SortConfig;
};

export interface GenericTableProps<T extends { id: string }> {
  tableTitle: string;
  addingTitle: string;
  columns: Column<T>[];
  actions?: TableActions;
  formComponent: React.ComponentType<{
    onSubmit: () => void;
  }>;
  requireDeleteConfirmation?: boolean;
  disabledActions?: ActionCellProps["disabledActions"];
  queryHook: QueryHook<T>;
  filterOptions: FilterOption[];
  additionalActions?: AdditionalAction[];
}
