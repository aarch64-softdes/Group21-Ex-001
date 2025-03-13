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
  transform?: (value: T[keyof T]) => string;
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

export interface AdditionalActionItem {
  label: string;
  handler: () => void;
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

export interface FormComponentProps<T> {
  onSubmit: (data: Partial<T>) => void;
  onCancel: () => void;
  id?: string;
  isLoading?: boolean;
  isEditing?: boolean;
}

export interface DetailComponentProps {
  id?: string;
}

export interface GenericTableProps<T extends { id: string }> {
  tableTitle: string;
  addingTitle: string;
  columns: Column<T>[];
  actions?: TableActions;
  formComponent: React.FC<FormComponentProps<T>>;
  detailComponent: React.FC<DetailComponentProps>;
  disabledActions?: {
    edit?: boolean;
    delete?: boolean;
  };
  queryHook: QueryHook<T>;
  filterOptions: FilterOption[];
  requireDeleteConfirmation?: boolean;
  additionalActions?: AdditionalAction[];
}

export interface ActionCellProps {
  requireDeleteConfirmation?: boolean;
  isDeleting?: boolean;
  onView?: () => void;
  onEdit?: () => void;
  onDelete?: () => void;
  disabledActions?: {
    view?: boolean;
    edit?: boolean;
    delete?: boolean;
  };
  additionalActions?: AdditionalActionItem[];
}
