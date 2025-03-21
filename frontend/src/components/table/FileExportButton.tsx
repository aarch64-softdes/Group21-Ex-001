import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { FileDown } from 'lucide-react';
import { useState } from 'react';

interface FileExportButtonProps {
  onExport: (format: string) => Promise<Blob>;
  disabled?: boolean;
  className?: string;
}

const FileExportButton = ({
  onExport,
  disabled = false,
  className,
}: FileExportButtonProps) => {
  const [isExporting, setIsExporting] = useState(false);

  const handleExport = async (format: string) => {
    try {
      setIsExporting(true);
      const blob = await onExport(format);

      // Create a download link
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
      a.href = url;
      a.download = `students_${timestamp}.${format}`;
      document.body.appendChild(a);
      a.click();

      // Clean up
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);

      console.log(`Export successful. File downloaded as ${a.download}`);
    } catch (error) {
      console.error('Export failed:', error);
    } finally {
      setIsExporting(false);
    }
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          variant='outline'
          size='sm'
          disabled={disabled || isExporting}
          className={`flex items-center gap-2 ${className}`}
        >
          <FileDown className='h-4 w-4' />
          {isExporting ? 'Exporting...' : 'Export'}
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align='end'>
        <DropdownMenuItem onClick={() => handleExport('csv')}>
          Export as CSV
        </DropdownMenuItem>
        <DropdownMenuItem onClick={() => handleExport('json')}>
          Export as JSON
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default FileExportButton;
