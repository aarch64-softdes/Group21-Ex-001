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
import { useTranslation } from 'react-i18next';

const AdjustmentDurationSettings: React.FC<{ className?: string }> = ({
  className,
}) => {
  const { t } = useTranslation(['setting', 'common']);
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
      return t('setting:adjustmentDuration.validation.required');
    }

    const numValue = parseInt(value);
    if (isNaN(numValue)) {
      return t('setting:adjustmentDuration.validation.mustBeNumber');
    }

    if (numValue < 1) {
      return t('setting:adjustmentDuration.validation.minDuration');
    }

    if (numValue > 90) {
      return t('setting:adjustmentDuration.validation.maxDuration');
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
      showSuccessToast(
        t('setting:messages.updated', {
          setting: t('setting:adjustmentDuration.title'),
        }),
      );
    } catch (err) {
      console.error('Failed to update adjustment duration:', err);
      showErrorToast(
        t('setting:messages.updateFailed', {
          setting: t('setting:adjustmentDuration.title'),
          error: getErrorMessage(err),
        }),
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
        <CardTitle>{t('setting:adjustmentDuration.title')}</CardTitle>
        <CardDescription>
          {t('setting:adjustmentDuration.description')}
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
                  {t('setting:adjustmentDuration.label')}
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
                    {t('setting:actions.save')}
                  </>
                ) : (
                  <>
                    <Pencil className='mr-2 h-4 w-4' />
                    {t('setting:actions.edit')}
                  </>
                )}
              </Button>
              {isEditing && (
                <Button variant='outline' onClick={handleCancel}>
                  {t('setting:actions.cancel')}
                </Button>
              )}
            </div>
            <div className='text-sm text-gray-500'>
              <p>{t('setting:adjustmentDuration.help')}</p>
            </div>
          </div>
        )}

        {/* Confirmation Dialog */}
        <Dialog open={showConfirmDialog} onOpenChange={setShowConfirmDialog}>
          <DialogContent className='w-160 p-8'>
            <DialogHeader>
              <DialogTitle>
                {t('setting:adjustmentDuration.confirmTitle')}
              </DialogTitle>
              <DialogDescription>
                {t('setting:adjustmentDuration.confirmDescription', {
                  duration: tempDuration,
                })}
              </DialogDescription>
            </DialogHeader>
            <DialogFooter>
              <Button
                variant='outline'
                onClick={() => setShowConfirmDialog(false)}
              >
                {t('setting:actions.cancel')}
              </Button>
              <Button
                onClick={handleSave}
                disabled={updateAdjustmentDuration.isPending}
              >
                {updateAdjustmentDuration.isPending ? (
                  <>
                    <Loader2 className='mr-2 h-4 w-4 animate-spin' />
                    {t('setting:actions.saving')}
                  </>
                ) : (
                  t('setting:actions.confirm')
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
