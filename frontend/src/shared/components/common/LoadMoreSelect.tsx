import * as React from 'react';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@ui/command';
import { Popover, PopoverContent, PopoverTrigger } from '@ui/popover';
import { Button } from '@ui/button';
import { ScrollArea } from '@ui/scroll-area';
import { Check, ChevronsUpDown, Loader2 } from 'lucide-react';
import { cn } from '@/shared/lib/utils';
import { FormControl } from '@ui/form';

export interface SelectItem {
  id: string;
  label: string;
  value: string;
}

interface LoadMoreSelectProps {
  value: string;
  onValueChange: (value: string) => void;
  placeholder?: string;
  items: SelectItem[];
  isLoading: boolean;
  hasMore: boolean;
  onLoadMore: () => void;
  isLoadingMore: boolean;
  emptyMessage?: string;
  disabled?: boolean;
  searchPlaceholder?: string;
  className?: string;
  onSearch?: (value: string) => void;
}

export function LoadMoreSelect({
  value,
  onValueChange,
  placeholder = 'Select...',
  items,
  isLoading,
  hasMore,
  onLoadMore,
  isLoadingMore,
  emptyMessage = 'No items found.',
  disabled = false,
  searchPlaceholder = 'Search...',
  className,
  onSearch,
}: LoadMoreSelectProps) {
  const [open, setOpen] = React.useState(false);
  const [localSearch, setLocalSearch] = React.useState('');

  const selectedItem = React.useMemo(
    () => items.find((item) => item.value === value),
    [items, value],
  );

  // Handle search with debounce
  React.useEffect(() => {
    const timer = setTimeout(() => {
      if (onSearch) {
        onSearch(localSearch);
      }
    }, 300);

    return () => clearTimeout(timer);
  }, [localSearch, onSearch]);

  return (
    <Popover open={open} onOpenChange={setOpen} modal={true}>
      <PopoverTrigger asChild>
        <FormControl>
          <Button
            variant='outline'
            role='combobox'
            aria-expanded={open}
            className={cn('w-full justify-between', className)}
            disabled={disabled || isLoading}
          >
            {selectedItem ? selectedItem.label : placeholder}
            {isLoading ? (
              <Loader2 className='ml-auto h-4 w-4 shrink-0 animate-spin opacity-50' />
            ) : (
              <ChevronsUpDown className='ml-auto h-4 w-4 shrink-0 opacity-50' />
            )}
          </Button>
        </FormControl>
      </PopoverTrigger>
      <PopoverContent className='w-full p-0'>
        <Command>
          <CommandInput
            placeholder={searchPlaceholder}
            value={localSearch}
            onValueChange={setLocalSearch}
          />
          <CommandList>
            {isLoading && items.length === 0 ? (
              <div className='py-6 text-center'>
                <Loader2 className='mx-auto h-6 w-6 animate-spin opacity-50' />
                <p className='text-sm text-muted-foreground mt-2'>
                  Loading items...
                </p>
              </div>
            ) : (
              <ScrollArea className='h-48'>
                <CommandEmpty>{emptyMessage}</CommandEmpty>
                <CommandGroup>
                  {items.map((item) => (
                    <CommandItem
                      key={item.id}
                      value={item.value}
                      onSelect={() => {
                        onValueChange(item.value);
                        setOpen(false);
                      }}
                    >
                      <Check
                        className={cn(
                          'mr-2 h-4 w-4',
                          value === item.value ? 'opacity-100' : 'opacity-0',
                        )}
                      />
                      {item.label}
                    </CommandItem>
                  ))}
                  {hasMore && (
                    <div className='py-2 px-2 text-center'>
                      <Button
                        variant='ghost'
                        size='sm'
                        onClick={(e) => {
                          e.preventDefault();
                          e.stopPropagation();
                          onLoadMore();
                        }}
                        disabled={isLoadingMore}
                        className='w-full'
                      >
                        {isLoadingMore ? (
                          <>
                            <Loader2 className='mr-2 h-4 w-4 animate-spin' />
                            Loading more...
                          </>
                        ) : (
                          'Load more'
                        )}
                      </Button>
                    </div>
                  )}
                </CommandGroup>
              </ScrollArea>
            )}
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  );
}

export default LoadMoreSelect;
