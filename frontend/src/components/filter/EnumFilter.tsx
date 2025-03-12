import { Button } from "@/components/ui/button";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { EnumFilterOption } from "@/types/filter";
import { useState } from "react";
import {
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "../ui/accordion";
import { Checkbox } from "@/components/ui/checkbox";

const EnumFilter: React.FC<EnumFilterOption> = ({
  label,
  labelIcon: LabelIcon,
  items,
  onChange,
  componentType = "popover",
}: EnumFilterOption) => {
  const [checkedIds, setCheckedIds] = useState<string[]>([]);

  const handleCheckboxChange = (id: string) => {
    const newCheckedIds = checkedIds.includes(id)
      ? checkedIds.filter((item) => item !== id)
      : [...checkedIds, id];

    setCheckedIds(newCheckedIds);

    // Event up
    onChange?.(newCheckedIds);
  };

  const clearCheckboxes = () => {
    setCheckedIds([]);

    // Event up
    onChange?.([]);
  };

  const hasSelectedItems = checkedIds.length > 0;

  if (componentType === "popover") {
    return (
      <Popover>
        <PopoverTrigger asChild>
          <Button variant="outline">
            {LabelIcon && <LabelIcon className="mr-2 h-4 w-4" />}
            {label}
          </Button>
        </PopoverTrigger>
        <PopoverContent className="w-full px-6">
          <div className="space-y-3">
            {items?.map(({ id, label, icon: Icon }) => (
              <div key={id} className="flex items-center space-x-3">
                <Checkbox
                  id={id}
                  checked={checkedIds.includes(id)}
                  onCheckedChange={() => handleCheckboxChange(id)}
                />
                <div className="flex items-center space-x-2">
                  <Icon className="h-4 w-4 text-gray-500" />
                  <label
                    htmlFor={id}
                    className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                  >
                    {label}
                  </label>
                </div>
              </div>
            ))}

            {hasSelectedItems && (
              <Button
                onClick={clearCheckboxes}
                variant="outline"
                className="h-8 w-full"
              >
                Clear All
              </Button>
            )}
          </div>
        </PopoverContent>
      </Popover>
    );
  } else {
    return (
      <AccordionItem value={String(label)} className="w-full">
        <AccordionTrigger>
          <div className="flex items-center gap-2">
            {LabelIcon && <LabelIcon className="mr-2 h-4 w-4" />}
            {label}
          </div>
        </AccordionTrigger>
        <AccordionContent>
          <div className="space-y-4">
            {items?.map(({ id, label, icon: Icon }) => (
              <div key={id} className="flex items-center space-x-3">
                <Checkbox
                  id={id}
                  checked={checkedIds.includes(id)}
                  onCheckedChange={() => handleCheckboxChange(id)}
                />
                <div className="flex items-center space-x-2">
                  <Icon className="h-4 w-4 text-gray-500" />
                  <label
                    htmlFor={id}
                    className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                  >
                    {label}
                  </label>
                </div>
              </div>
            ))}

            {hasSelectedItems && (
              <Button
                onClick={clearCheckboxes}
                variant="outline"
                className="h-8 w-full"
              >
                Clear All
              </Button>
            )}
          </div>
        </AccordionContent>
      </AccordionItem>
    );
  }
};
export default EnumFilter;
