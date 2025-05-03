import { Button } from '@ui/button';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@ui/select';
import {
  ChevronLeft,
  ChevronRight,
  ChevronsLeft,
  ChevronsRight,
} from 'lucide-react';
import React from 'react';
import { useTranslation } from 'react-i18next';

import { cn } from '@/shared/lib/utils';
import { TablePaginationProps } from '@/core/types/table';

const TablePagination = ({
  currentPage = 1,
  totalPages = 1,
  totalItems = 0,
  pageSize,
  onPageChange,
  onPageSizeChange,
}: TablePaginationProps) => {
  const { t } = useTranslation('common');

  React.useEffect(() => {
    if (currentPage > totalPages) {
      onPageChange(totalPages);
    }
  }, [currentPage, totalPages, onPageChange]);

  const getPageNumbers = () => {
    const pages = [];
    const showPages = 5;

    let start = Math.max(1, currentPage - Math.floor(showPages / 2));
    let end = Math.min(totalPages, start + showPages - 1);

    if (end === totalPages) {
      start = Math.max(1, end - showPages + 1);
    }

    for (let i = start; i <= end; i++) {
      pages.push(i);
    }

    return pages;
  };

  return (
    <div className='flex items-center justify-between px-4 py-4'>
      <div className='flex items-center space-x-6'>
        <div className='flex items-center space-x-2'>
          <span className='text-sm text-gray-700'>
            {t('pagination.rowsPerPage')}
          </span>
          <Select
            value={pageSize.toString()}
            onValueChange={(value) => {
              onPageSizeChange?.(Number(value));
              onPageChange(1);
            }}
          >
            <SelectTrigger className='h-8 w-20'>
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              {[10, 20, 30, 40, 50].map((size) => (
                <SelectItem key={size} value={size.toString()}>
                  {size}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        <span className='pl-2 text-sm text-gray-700'>
          {t('pagination.showing', {
            from: Math.min(
              Math.max(0, (currentPage - 1) * pageSize + 1),
              totalItems,
            ),
            to: Math.min(currentPage * pageSize, totalItems),
            total: totalItems,
          })}
        </span>
      </div>

      <div className='flex items-center space-x-2'>
        <Button
          variant='outline'
          size='icon'
          onClick={() => onPageChange(1)}
          disabled={currentPage === 1 || totalItems === 0}
          aria-label={t('pagination.firstPage')}
          className='h-8 w-8'
        >
          <ChevronsLeft className='h-4 w-4' />
        </Button>

        <Button
          variant='outline'
          size='icon'
          onClick={() => onPageChange(currentPage - 1)}
          disabled={currentPage === 1 || totalItems === 0}
          aria-label={t('pagination.previousPage')}
          className='h-8 w-8'
        >
          <ChevronLeft className='h-4 w-4' />
        </Button>

        {getPageNumbers().map((page) => (
          <Button
            key={page}
            variant={currentPage === page ? 'default' : 'outline'}
            onClick={() => onPageChange(page)}
            aria-label={t('pagination.page', { page })}
            aria-current={currentPage === page ? 'page' : undefined}
            className={cn(
              'h-8 w-8',
              currentPage === page
                ? 'bg-blue-500 text-white hover:bg-blue-400'
                : '',
            )}
          >
            {page}
          </Button>
        ))}

        <Button
          variant='outline'
          size='icon'
          onClick={() => onPageChange(currentPage + 1)}
          disabled={currentPage === totalPages || totalItems === 0}
          aria-label={t('pagination.nextPage')}
          className='h-8 w-8'
        >
          <ChevronRight className='h-4 w-4' />
        </Button>

        <Button
          variant='outline'
          size='icon'
          onClick={() => onPageChange(totalPages)}
          disabled={currentPage === totalPages || totalItems === 0}
          aria-label={t('pagination.lastPage')}
          className='h-8 w-8'
        >
          <ChevronsRight className='h-4 w-4' />
        </Button>
      </div>
    </div>
  );
};

export default TablePagination;
