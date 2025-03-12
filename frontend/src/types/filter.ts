export interface CheckboxItem {
  id: string;
  label: string;
  icon: React.FC<React.SVGProps<SVGSVGElement>>;
}

export interface BaseFilterOption {
  id: string;
  label?: string;
  labelIcon?: React.FC<React.SVGProps<SVGSVGElement>>;
  type: "enum" | "range" | "date" | "search";
  componentType?: "popover" | "accordion";
}

// export interface RangeFilterOption extends BaseFilterOption {
//   min: number;
//   max: number;
//   value?: [number, number];
//   step?: number;
//   onChange?: (value: [number, number]) => void;
//   type: 'range';
// }

// export interface EnumFilterOption extends BaseFilterOption {
//   items?: CheckboxItem[];
//   onChange?: (ids: string[]) => void;
//   type: 'enum';
// }

// export interface DateRangeFilterOption extends BaseFilterOption {
//   minDate: Date;
//   maxDate: Date;
//   onChange?: (range: [Date, Date]) => void;
//   value?: [Date, Date];
//   type: 'date';
// }

export interface SearchFilterOption extends BaseFilterOption {
  value?: string;
  onChange?: (value: string) => void;
  type: "search";
}

export type FilterParams = string[] | [number, number] | string | [Date, Date];

export type FilterOption =
  // | RangeFilterOption
  // | EnumFilterOption
  // | DateRangeFilterOption
  SearchFilterOption;
