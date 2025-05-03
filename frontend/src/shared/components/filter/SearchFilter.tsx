import {
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from '@ui/accordion';
import { Button } from '@ui/button';
import { Input } from '@ui/input';
import { Popover, PopoverContent, PopoverTrigger } from '@ui/popover';
import { useDebounce } from '@/shared/hooks/useDebounce';
import { SearchFilterOption } from '@/core/types/filter';
import { Search, X } from 'lucide-react';
import { useCallback, useEffect, useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';

const SearchFilter: React.FC<SearchFilterOption> = ({
  id,
  label,
  labelIcon: LabelIcon,
  value = '',
  onChange,
  componentType = 'popover',
  placeholder,
}) => {
  const { t } = useTranslation('common');
  const [localValue, setLocalValue] = useState<string>(value);
  const debouncedValue = useDebounce(localValue, 300);

  useEffect(() => {
    if (value !== localValue) {
      setLocalValue(value);
    }
  }, [value]);

  useEffect(() => {
    if (value == '') {
      onChange?.('');
    }

    if (debouncedValue !== value) {
      onChange?.(debouncedValue);
    }
  }, [debouncedValue, value]);

  const handleInputChange = useCallback(
    (event: React.ChangeEvent<HTMLInputElement>) => {
      setLocalValue(event.target.value);
    },
    [],
  );

  const handleClear = useCallback(() => {
    setLocalValue('');
  }, []);

  const hasValue = localValue.length > 0;

  const searchContent = useMemo(
    () => (
      <div className='space-y-4'>
        <div className='relative'>
          <Search className='absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground' />
          <Input
            id={id}
            type='text'
            value={localValue}
            onChange={handleInputChange}
            placeholder={placeholder || t('table.search')}
            className='w-56 pl-9'
          />
          {hasValue && (
            <button
              onClick={handleClear}
              className='absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground'
              aria-label={t('actions.clear')}
            >
              <X className='h-4 w-4' />
            </button>
          )}
        </div>
      </div>
    ),
    [localValue, handleInputChange, hasValue, handleClear, id, label, t],
  );

  const triggerContent = (
    <div className='flex items-center gap-2'>
      {LabelIcon && <LabelIcon className='mr-2 h-4 w-4' />}
      {label || t('actions.search')}
      {hasValue && (
        <span className='ml-2 text-sm text-muted-foreground'>
          "{localValue}"
        </span>
      )}
    </div>
  );

  if (componentType === 'popover') {
    return (
      <Popover>
        <PopoverTrigger asChild>
          <Button variant='outline'>{triggerContent}</Button>
        </PopoverTrigger>
        <PopoverContent className='w-[320px] p-4'>
          {searchContent}
        </PopoverContent>
      </Popover>
    );
  }

  return (
    <AccordionItem value={String(label)} className='w-full'>
      <AccordionTrigger>{triggerContent}</AccordionTrigger>
      <AccordionContent>{searchContent}</AccordionContent>
    </AccordionItem>
  );
};

export default SearchFilter;
