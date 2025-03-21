import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { FileUp, Upload } from 'lucide-react';
import { useRef, useState } from 'react';

interface FileImportButtonProps {
  onImport: (format: string, file: File) => Promise<void>;
  onSuccess?: () => void;
  disabled?: boolean;
  className?: string;
}

const FileImportButton = ({
  onImport,
  disabled = false,
  className,
}: FileImportButtonProps) => {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isImporting, setIsImporting] = useState(false);
  const [selectedFormat, setSelectedFormat] = useState<string>('csv');
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setSelectedFile(e.target.files[0]);
    }
  };

  const handleImport = async () => {
    if (!selectedFile || !selectedFormat) {
      console.error('Please select a file and format');
      return;
    }

    try {
      setIsImporting(true);
      await onImport(selectedFormat, selectedFile);
      console.log('Import successful!');
      setIsDialogOpen(false);
      setSelectedFile(null);
    } catch (error) {
      console.error('Import failed:', error);
    } finally {
      setIsImporting(false);
    }
  };

  const resetFileInput = () => {
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
    setSelectedFile(null);
  };

  return (
    <>
      <Button
        variant='outline'
        size='sm'
        disabled={disabled}
        className={`flex items-center gap-2 ${className}`}
        onClick={() => setIsDialogOpen(true)}
      >
        <FileUp className='h-4 w-4' />
        Import
      </Button>

      <Dialog
        open={isDialogOpen}
        onOpenChange={(open) => {
          setIsDialogOpen(open);
          if (!open) resetFileInput();
        }}
      >
        <DialogContent className='sm:max-w-[425px]'>
          <DialogHeader>
            <DialogTitle>Import Students</DialogTitle>
            <DialogDescription>
              Upload a CSV or JSON file with student data.
            </DialogDescription>
          </DialogHeader>

          <div className='grid gap-4 py-4'>
            <div className='grid gap-2'>
              <label htmlFor='format' className='text-sm font-medium'>
                File Format
              </label>
              <Select
                value={selectedFormat}
                onValueChange={setSelectedFormat}
                disabled={isImporting}
              >
                <SelectTrigger>
                  <SelectValue placeholder='Select format' />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value='csv'>CSV</SelectItem>
                  <SelectItem value='json'>JSON</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className='grid gap-2'>
              <label htmlFor='file' className='text-sm font-medium'>
                File
              </label>
              <div className='flex items-center gap-2'>
                <input
                  ref={fileInputRef}
                  type='file'
                  id='file'
                  accept={selectedFormat === 'csv' ? '.csv' : '.json'}
                  onChange={handleFileChange}
                  disabled={isImporting}
                  className='hidden'
                />
                <Button
                  variant='outline'
                  onClick={() => fileInputRef.current?.click()}
                  disabled={isImporting}
                  className='w-full justify-start text-left'
                >
                  <Upload className='mr-2 h-4 w-4' />
                  {selectedFile ? selectedFile.name : 'Choose a file'}
                </Button>
              </div>
            </div>
          </div>

          <DialogFooter>
            <Button
              variant='outline'
              onClick={() => setIsDialogOpen(false)}
              disabled={isImporting}
            >
              Cancel
            </Button>
            <Button
              onClick={handleImport}
              disabled={!selectedFile || isImporting}
            >
              {isImporting ? 'Importing...' : 'Import'}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
};

export default FileImportButton;
