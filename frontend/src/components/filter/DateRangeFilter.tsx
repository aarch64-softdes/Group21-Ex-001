import {
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion";
import { Button } from "@/components/ui/button";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { useDebounce } from "@/hooks/useDebounce";
import { DateRangeFilterOption } from "@/types/filter";
import { format, isAfter, isBefore, isValid, parse } from "date-fns";
import { Calendar } from "lucide-react";
import { useCallback, useEffect, useMemo, useState } from "react";

const DateRangeFilter: React.FC<DateRangeFilterOption> = ({
  label,
  labelIcon: LabelIcon,
  minDate = new Date(2000, 0, 1),
  maxDate = new Date(2030, 11, 31),
  onChange,
  value = [minDate, maxDate],
  componentType = "popover",
}) => {
  const [localRange, setLocalRange] = useState<[Date, Date]>(value);
  const [inputValues, setInputValues] = useState({
    start: value[0]
      ? format(value[0], "yyyy-MM-dd")
      : format(minDate, "yyyy-MM-dd"),
    end: value[1]
      ? format(value[1], "yyyy-MM-dd")
      : format(maxDate, "yyyy-MM-dd"),
  });

  const debouncedRange = useDebounce(localRange, 500);

  useEffect(() => {
    if (
      debouncedRange[0]?.getTime() !== value[0]?.getTime() ||
      debouncedRange[1]?.getTime() !== value[1]?.getTime()
    ) {
      onChange?.(debouncedRange);
    }
  }, [debouncedRange, onChange, value]);

  useEffect(() => {
    setLocalRange(value);
    setInputValues({
      start: value[0] ? format(value[0], "yyyy-MM-dd") : "",
      end: value[1] ? format(value[1], "yyyy-MM-dd") : "",
    });
  }, [value]);

  const handleDateChange = useCallback(
    (dateStr: string, isStart: boolean) => {
      setInputValues((prev) => ({
        ...prev,
        [isStart ? "start" : "end"]: dateStr,
      }));

      const parsedDate = parse(dateStr, "yyyy-MM-dd", new Date());

      if (!isValid(parsedDate)) return;

      setLocalRange((currentRange) => {
        if (isStart) {
          // Ensure start date is not after end date and within bounds
          if (currentRange[1] && isAfter(parsedDate, currentRange[1]))
            return currentRange;
          if (isBefore(parsedDate, minDate) || isAfter(parsedDate, maxDate))
            return currentRange;
          return [parsedDate, currentRange[1]];
        } else {
          // Ensure end date is not before start date and within bounds
          if (currentRange[0] && isBefore(parsedDate, currentRange[0]))
            return currentRange;
          if (isBefore(parsedDate, minDate) || isAfter(parsedDate, maxDate))
            return currentRange;
          return [currentRange[0], parsedDate];
        }
      });
    },
    [minDate, maxDate]
  );

  const clearRange = useCallback(() => {
    setLocalRange([minDate, maxDate]);
    setInputValues({
      start: format(minDate, "yyyy-MM-dd"),
      end: format(maxDate, "yyyy-MM-dd"),
    });
  }, []);

  const hasCustomRange = () => {
    return (
      localRange[0]?.getTime() !== minDate.getTime() ||
      localRange[1]?.getTime() !== maxDate.getTime()
    );
  };

  const filterContent = useMemo(
    () => (
      <div className="space-y-4">
        <div className="grid grid-rows-2 gap-4">
          <div className="space-y-2">
            <label className="text-sm text-gray-500">Start Date</label>
            <input
              type="date"
              value={inputValues.start}
              min={format(minDate, "yyyy-MM-dd")}
              max={format(maxDate, "yyyy-MM-dd")}
              onChange={(e) => handleDateChange(e.target.value, true)}
              className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
            />
          </div>
          <div className="space-y-2">
            <label className="text-sm text-gray-500">End Date</label>
            <input
              type="date"
              value={inputValues.end}
              min={format(minDate, "yyyy-MM-dd")}
              max={format(maxDate, "yyyy-MM-dd")}
              onChange={(e) => handleDateChange(e.target.value, false)}
              className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
            />
          </div>
        </div>

        {hasCustomRange() && (
          <Button onClick={clearRange} variant="outline" className="h-8 w-full">
            Clear Range
          </Button>
        )}
      </div>
    ),
    [
      inputValues,
      minDate,
      maxDate,
      hasCustomRange,
      handleDateChange,
      clearRange,
    ]
  );

  if (componentType === "popover") {
    return (
      <Popover>
        <PopoverTrigger asChild>
          <Button variant="outline">
            {LabelIcon ? (
              <LabelIcon className="mr-2 h-4 w-4" />
            ) : (
              <Calendar className="mr-2 h-4 w-4" />
            )}
            {label}
          </Button>
        </PopoverTrigger>
        <PopoverContent className="w-[320px] p-4">
          {filterContent}
        </PopoverContent>
      </Popover>
    );
  }

  return (
    <AccordionItem value={String(label)} className="w-full">
      <AccordionTrigger>
        <div className="flex items-center gap-2">
          {LabelIcon ? (
            <LabelIcon className="mr-2 h-4 w-4" />
          ) : (
            <Calendar className="mr-2 h-4 w-4" />
          )}
          {label}
        </div>
      </AccordionTrigger>
      <AccordionContent>{filterContent}</AccordionContent>
    </AccordionItem>
  );
};

export default DateRangeFilter;
