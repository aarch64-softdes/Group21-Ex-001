import { Skeleton } from "@/components/ui/skeleton";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { cn } from "@/lib/utils";

interface TableSkeletonProps {
  columns?: number;
  rows?: number;
  variant?: "all" | "body";
}

const SkeletonTable = ({
  columns = 5,
  rows = 5,
  variant = "all",
}: TableSkeletonProps) => {
  let color = "bg-gray-300";

  if (variant === "body") {
    return (
      <TableBody>
        {Array.from({ length: rows }).map((_, rowIndex) => (
          <TableRow key={rowIndex}>
            {Array.from({ length: columns }).map((_, colIndex) => (
              <TableCell key={colIndex} className="py-1">
                <Skeleton className={cn("h-4 w-full", color)} />
              </TableCell>
            ))}
            <TableCell className="py-1">
              <Skeleton className={cn("h-8 w-8 rounded-full", color)} />
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    );
  }

  return (
    <Table>
      <TableHeader>
        <TableRow>
          {Array.from({ length: columns }).map((_, index) => (
            <TableHead key={index}>
              <Skeleton className={cn("h-4 w-[100px]", color)} />
            </TableHead>
          ))}
          <TableHead className="w-4">
            <Skeleton className={cn("h-4 w-[60px]", color)} />
          </TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {Array.from({ length: rows }).map((_, rowIndex) => (
          <TableRow key={rowIndex}>
            {Array.from({ length: columns }).map((_, colIndex) => (
              <TableCell key={colIndex} className="py-1">
                <Skeleton className={cn("h-4 w-full", color)} />
              </TableCell>
            ))}
            <TableCell className="py-1">
              <Skeleton className={cn("h-8 w-8 rounded-full", color)} />
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
};

export default SkeletonTable;
