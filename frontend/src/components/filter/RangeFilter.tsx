import {
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from '@/components/common/ui/accordion';
import { Button } from '@/components/common/ui/button';
import { Input } from '@/components/common/ui/input'; // Add this import
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/common/ui/popover';
import { Slider } from '@/components/common/ui/slider';
import { useDebounce } from '@/hooks/useDebounce';
import { RangeFilterOption } from '@/types/filter';
import { useCallback, useEffect, useMemo, useState } from 'react';

const RangeFilter: React.FC<RangeFilterOption> = ({
  label,
  labelIcon: LabelIcon,
  min,
  max,
  step = 1,
  onChange,
  value = [min, max],
  componentType = 'popover',
}) => {
  const [localRange, setLocalRange] = useState<[number, number]>(value);
  const [inputValues, setInputValues] = useState({
    min: value[0].toString(),
    max: value[1].toString(),
  });

  const debouncedRange = useDebounce(localRange, 500);

  // Only update parent when debounced value changes
  useEffect(() => {
    if (debouncedRange[0] !== value[0] || debouncedRange[1] !== value[1]) {
      onChange?.(debouncedRange);
    }
  }, [debouncedRange, onChange, value]);

  // Sync with parent value changes
  useEffect(() => {
    setLocalRange(value);
    setInputValues({
      min: value[0].toString(),
      max: value[1].toString(),
    });
  }, [value]);

  const handleSliderChange = useCallback((newValues: number[]) => {
    const newRange: [number, number] = [newValues[0], newValues[1]];
    setLocalRange(newRange);
    setInputValues({
      min: newRange[0].toString(),
      max: newRange[1].toString(),
    });
  }, []);

  const handleInputChange = useCallback(
    (value: string, isMin: boolean) => {
      setInputValues((prev) => ({
        ...prev,
        [isMin ? 'min' : 'max']: value,
      }));

      const numValue = parseFloat(value);
      if (isNaN(numValue)) return;

      setLocalRange((currentRange) => {
        if (isMin) {
          return [
            Math.min(Math.max(numValue, min), currentRange[1]),
            currentRange[1],
          ] as [number, number];
        } else {
          return [
            currentRange[0],
            Math.max(Math.min(numValue, max), currentRange[0]),
          ] as [number, number];
        }
      });
    },
    [min, max],
  );

  const clearRange = useCallback(() => {
    const defaultRange: [number, number] = [min, max];
    setLocalRange(defaultRange);
    setInputValues({
      min: min.toString(),
      max: max.toString(),
    });
  }, [min, max, onChange]);

  const hasCustomRange = localRange[0] !== min || localRange[1] !== max;

  const filterContent = useMemo(
    () => (
      <>
        <Slider
          min={min}
          max={max}
          step={step}
          value={[localRange[0], localRange[1]]}
          onValueChange={handleSliderChange}
          className='pt-4'
        />

        <div className='flex items-center space-x-2'>
          <Input
            type='number'
            value={inputValues.min}
            onChange={(e) => handleInputChange(e.target.value, true)}
            min={min}
            max={max}
            step={step}
            className='w-20'
          />
          <span className='text-muted-foreground'>to</span>
          <Input
            type='number'
            value={inputValues.max}
            onChange={(e) => handleInputChange(e.target.value, false)}
            min={min}
            max={max}
            step={step}
            className='w-20'
          />
        </div>

        {hasCustomRange && (
          <Button onClick={clearRange} variant='outline' className='h-8 w-full'>
            Clear Range
          </Button>
        )}
      </>
    ),
    [localRange],
  );

  if (componentType === 'popover') {
    return (
      <Popover>
        <PopoverTrigger asChild>
          <Button variant='outline'>
            {LabelIcon && <LabelIcon className='mr-2 h-4 w-4' />}
            {label}
            {hasCustomRange && (
              <span className='ml-2 text-sm text-muted-foreground'>
                {localRange[0]} - {localRange[1]}
              </span>
            )}
          </Button>
        </PopoverTrigger>
        <PopoverContent className='w-[280px] p-4'>
          <div className='space-y-5'>{filterContent}</div>
        </PopoverContent>
      </Popover>
    );
  }

  return (
    <AccordionItem value={String(label)} className='w-full'>
      <AccordionTrigger>
        <div className='flex items-center gap-2'>
          {LabelIcon && <LabelIcon className='mr-2 h-4 w-4' />}
          {label}
          {hasCustomRange && (
            <span className='mx-2 text-sm text-muted-foreground'>
              {localRange[0]} - {localRange[1]}
            </span>
          )}
        </div>
      </AccordionTrigger>

      <AccordionContent>
        <div className='space-y-5'>{filterContent}</div>
      </AccordionContent>
    </AccordionItem>
  );
};

export default RangeFilter;
