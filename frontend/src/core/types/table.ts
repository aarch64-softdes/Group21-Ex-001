import { UseQueryResult } from '@tanstack/react-query';
import { ApiResponse } from './apiResponse';
import { FilterOption, FilterParams } from './filter';
import { ReactElement } from 'react';

export type SortConfig = {
  key: string | null;
  direction: 'asc' | 'desc' | null;
};

export type SortProps = {
  columnKey: string;
  columnHeader: string;
  sortConfig: SortConfig;
  onSort: (key: string, direction: 'asc' | 'desc' | null) => void;
  sortable?: boolean;
};

export type ColumnStyle = {
  width?: string;
  minWidth?: string;
  maxWidth?: string;
};

export interface Column<T> {
  header: string;
  key: keyof T | string;
  nested?: boolean;
  isDefaultSort?: boolean;
  sortable?: boolean;
  style?: ColumnStyle;
  transform?: (value: any, row?: T) => string; // Use any to allow for any type of value
}

export type SaveAction = (id: string, updatedData: any) => void | Promise<void>;
export type DeleteAction = (id: string) => void | Promise<void>;
export type AddAction = (data: any) => void | Promise<void>;

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
  disabled?: (data: any) => boolean;
}

export interface AdditionalActionItem {
  label: string;
  handler: () => void;
  disabled?: boolean;
}

export type QueryHook<T> = (
  data: QueryHookParams,
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

export interface FormComponentPropsWithoutType {
  onSubmit: (data: any) => void;
  onCancel: () => void;
  id?: string;
  isLoading?: boolean;
  isEditing?: boolean;
}

export interface DetailComponentProps {
  id?: string;
}

export interface GenericTableProps<T extends { id: string }> {
  tableTitle?: string;
  columns: Column<T>[];
  addAction: {
    onAdd: AddAction;
    disabled?: boolean;
    title?: string;
  };
  actionCellProperties: {
    requireDeleteConfirmation?: boolean;
    edit: {
      onSave: SaveAction;
      disabled?: boolean;
    };
    delete: {
      onDelete: DeleteAction;
      disabled?: boolean;
    };
    formComponent?: React.FC<FormComponentProps<T>>;
    detailComponent?: React.FC<DetailComponentProps>;
    additionalActions?: AdditionalAction[];
  };
  customActionCellComponent?: React.FC<any>;
  queryHook: QueryHook<T>;
  filterOptions: FilterOption[];
  tableOptions?: ReactElement<{ disabled?: boolean }>[];
  disablePagination?: boolean;
  metadata?: any;
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
