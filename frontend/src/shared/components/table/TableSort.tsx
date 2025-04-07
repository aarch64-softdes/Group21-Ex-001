import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@ui/dropdown-menu';
import { SortProps } from '@/core/types/table';
import { ArrowDown, ArrowUp, ChevronsUpDown, X } from 'lucide-react';

const TableSort = ({
  columnKey,
  columnHeader,
  sortConfig,
  onSort,
  sortable,
}: SortProps) => {
  if (!sortable) {
    return <span>{columnHeader}</span>;
  }

  const getSortIcon = () => {
    if (sortConfig.key !== columnKey)
      return <ChevronsUpDown className='h-4 w-4' />;
    if (sortConfig.direction === 'asc') return <ArrowUp className='h-4 w-4' />;
    if (sortConfig.direction === 'desc')
      return <ArrowDown className='h-4 w-4' />;
    return <ChevronsUpDown className='h-4 w-4' />;
  };

  const isCurrentColumn = sortConfig.key === columnKey;

  return (
    <DropdownMenu modal={false}>
      <DropdownMenuTrigger className='flex items-center gap-1 focus:outline-none'>
        {columnHeader}
        {getSortIcon()}
      </DropdownMenuTrigger>
      <DropdownMenuContent align='start'>
        <DropdownMenuItem onClick={() => onSort(columnKey, 'asc')}>
          <ArrowUp className='h-4 w-4' />
          Ascending
        </DropdownMenuItem>
        <DropdownMenuItem onClick={() => onSort(columnKey, 'desc')}>
          <ArrowDown className='h-4 w-4' />
          Descending
        </DropdownMenuItem>
        {isCurrentColumn && (
          <>
            <DropdownMenuSeparator />
            <DropdownMenuItem onClick={() => onSort(columnKey, null)}>
              <X className='h-4 w-4' />
              Clear
            </DropdownMenuItem>
          </>
        )}
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default TableSort;
