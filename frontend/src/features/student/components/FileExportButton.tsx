import { Button } from '@ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@ui/dropdown-menu';
import { FileDown } from 'lucide-react';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';

interface FileExportButtonProps {
  onExport: (format: string) => Promise<Blob>;
  disabled?: boolean;
  className?: string;
  prefix?: string;
}

const FileExportButton = ({
  onExport,
  disabled = false,
  className,
  prefix,
}: FileExportButtonProps) => {
  const { t } = useTranslation('common');
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
      a.download = `${prefix}_${timestamp}.${format}`;
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
          {isExporting
            ? t('common:actions.export.exporting')
            : t('common:actions.export.title')}
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align='end'>
        <DropdownMenuItem onClick={() => handleExport('csv')}>
          {t('common:actions.export.asCsv')}
        </DropdownMenuItem>
        <DropdownMenuItem onClick={() => handleExport('json')}>
          {t('common:actions.export.asJson')}
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default FileExportButton;
