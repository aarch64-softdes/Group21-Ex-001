import { Button } from '@ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@ui/card';
import { Input } from '@ui/input';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@ui/dialog';
import { useState } from 'react';
import { Pencil, Save, Loader2 } from 'lucide-react';
import { cn, getErrorMessage } from '@/shared/lib/utils';
import { showErrorToast, showSuccessToast } from '@/shared/lib/toast-utils';
import {
  useAdjustmentDurationSetting,
  useUpdateAdjustmentDurationSetting,
} from '@/features/settings/api/useSettingsApi';

const AdjustmentDurationSettings: React.FC<{ className?: string }> = ({
  className,
}) => {
  const { data, isLoading } = useAdjustmentDurationSetting();
  const updateAdjustmentDuration = useUpdateAdjustmentDurationSetting();

  const [isEditing, setIsEditing] = useState(false);
  const [tempDuration, setTempDuration] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);

  // When data is loaded, set the temporary duration
  if (data && tempDuration === '' && !isEditing) {
    setTempDuration(data.adjustmentDuration || '');
  }

  const validateDuration = (value: string): string | null => {
    if (!value.trim()) {
      return 'Duration is required';
    }

    const numValue = parseInt(value);
    if (isNaN(numValue)) {
      return 'Duration must be a number';
    }

    if (numValue < 1) {
      return 'Duration must be at least 1 day';
    }

    if (numValue > 90) {
      return 'Duration must not exceed 90 days';
    }

    return null;
  };

  const handleEditToggle = () => {
    if (isEditing) {
      // Validate before showing confirmation
      const validationError = validateDuration(tempDuration);
      if (validationError) {
        setError(validationError);
        return;
      }
      setShowConfirmDialog(true);
    } else {
      setIsEditing(true);
    }
  };

  const handleSave = async () => {
    setShowConfirmDialog(false);

    try {
      await updateAdjustmentDuration.mutateAsync({
        adjustmentDuration: tempDuration,
      });
      setIsEditing(false);
      setError(null);
      showSuccessToast('Adjustment duration updated successfully');
    } catch (err) {
      console.error('Failed to update adjustment duration:', err);
      showErrorToast(
        'Failed to update adjustment duration: ' + getErrorMessage(err),
      );
    }
  };

  const handleCancel = () => {
    if (data) {
      setTempDuration(data.adjustmentDuration || '');
    }
    setIsEditing(false);
    setError(null);
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTempDuration(e.target.value);
    setError(null);
  };

  return (
    <Card className={cn('w-160', className)}>
      <CardHeader>
        <CardTitle>Enrollment Adjustment Period</CardTitle>
        <CardDescription>
          Configure the number of days students can adjust their course
          enrollments after the start date
        </CardDescription>
      </CardHeader>
      <CardContent>
        {isLoading ? (
          <div className='flex items-center justify-center p-4'>
            <Loader2 className='h-8 w-8 animate-spin' />
          </div>
        ) : (
          <div className='space-y-4'>
            <div className='flex items-end gap-4'>
              <div className='flex-1'>
                <label
                  htmlFor='adjustmentDuration'
                  className='block text-sm font-medium text-gray-700 mb-1'
                >
                  Adjustment Period (days)
                </label>
                <Input
                  id='adjustmentDuration'
                  type='number'
                  value={tempDuration}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                  placeholder='7'
                  className={error ? 'border-red-500' : ''}
                />
                {error && <p className='text-red-500 text-sm mt-1'>{error}</p>}
              </div>
              <Button
                onClick={handleEditToggle}
                variant='default'
                disabled={updateAdjustmentDuration.isPending}
              >
                {isEditing ? (
                  <>
                    <Save className='mr-2 h-4 w-4' />
                    Save
                  </>
                ) : (
                  <>
                    <Pencil className='mr-2 h-4 w-4' />
                    Edit
                  </>
                )}
              </Button>
              {isEditing && (
                <Button variant='outline' onClick={handleCancel}>
                  Cancel
                </Button>
              )}
            </div>
            <div className='text-sm text-gray-500'>
              <p>
                Students will be able to enroll or unenroll from courses during
                this period after the course start date.
              </p>
            </div>
          </div>
        )}

        {/* Confirmation Dialog */}
        <Dialog open={showConfirmDialog} onOpenChange={setShowConfirmDialog}>
          <DialogContent className='w-160 p-8'>
            <DialogHeader>
              <DialogTitle>Confirm Adjustment Period Change</DialogTitle>
              <DialogDescription>
                You are about to change the enrollment adjustment period to{' '}
                <strong>{tempDuration} days</strong>. This will affect all
                course enrollment periods. Are you sure?
              </DialogDescription>
            </DialogHeader>
            <DialogFooter>
              <Button
                variant='outline'
                onClick={() => setShowConfirmDialog(false)}
              >
                Cancel
              </Button>
              <Button
                onClick={handleSave}
                disabled={updateAdjustmentDuration.isPending}
              >
                {updateAdjustmentDuration.isPending ? (
                  <>
                    <Loader2 className='mr-2 h-4 w-4 animate-spin' />
                    Saving...
                  </>
                ) : (
                  'Confirm'
                )}
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </CardContent>
    </Card>
  );
};

export default AdjustmentDurationSettings;
