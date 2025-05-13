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
  useEmailDomainSetting,
  useUpdateEmailDomainSetting,
} from '@/features/settings/api/useSettingsApi';
import { useState } from 'react';
import { Pencil, Save } from 'lucide-react';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@ui/dialog';
import { Loader2 } from 'lucide-react';
import { cn, getErrorMessage } from '@/shared/lib/utils';
import { showErrorToast, showSuccessToast } from '@/shared/lib/toast-utils';
import { useTranslation } from 'react-i18next';

const EmailDomainSettings: React.FC<{ className?: string }> = ({
  className,
}) => {
  const { t } = useTranslation(['setting', 'common']);
  const { data, isLoading } = useEmailDomainSetting();
  const updateEmailDomain = useUpdateEmailDomainSetting();

  const [isEditing, setIsEditing] = useState(false);
  const [tempDomain, setTempDomain] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);

  // When data is loaded, set the temporary domain
  if (data && tempDomain === '' && !isEditing) {
    setTempDomain(data.domain || '');
  }

  const validateEmailDomain = (domain: string): string | null => {
    if (!domain.startsWith('@')) {
      return t('setting:emailDomain.validation.mustStartWithAt');
    }

    // Simple regex to validate domain format after the @
    const domainRegex =
      /^@[a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9](?:\.[a-zA-Z]{2,})+$/;
    if (!domainRegex.test(domain)) {
      return t('setting:emailDomain.validation.invalidFormat');
    }

    return null;
  };

  const handleEditToggle = () => {
    if (isEditing) {
      // Validate before showing confirmation
      const validationError = validateEmailDomain(tempDomain);
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
      await updateEmailDomain.mutateAsync({ domain: tempDomain });
      setIsEditing(false);
      setError(null);
      showSuccessToast(
        t('setting:messages.updated', {
          setting: t('setting:emailDomain.title'),
        }),
      );
    } catch (err) {
      console.error('Failed to update email domain:', err);
      showErrorToast(
        t('setting:messages.updateFailed', {
          setting: t('setting:emailDomain.title'),
          error: getErrorMessage(err),
        }),
      );
    }
  };

  const handleCancel = () => {
    if (data) {
      setTempDomain(data.domain || '');
    }
    setIsEditing(false);
    setError(null);
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTempDomain(e.target.value);
    setError(null);
  };

  return (
    <Card className={cn('w-160', className)}>
      <CardHeader>
        <CardTitle>{t('setting:emailDomain.title')}</CardTitle>
        <CardDescription>
          {t('setting:emailDomain.description')}
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
                  htmlFor='emailDomain'
                  className='block text-sm font-medium text-gray-700 mb-1'
                >
                  {t('setting:emailDomain.label')}
                </label>
                <Input
                  id='emailDomain'
                  value={tempDomain}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                  placeholder={t('setting:emailDomain.placeholder')}
                  className={error ? 'border-red-500' : ''}
                />
                {error && <p className='text-red-500 text-sm mt-1'>{error}</p>}
              </div>
              <Button
                onClick={handleEditToggle}
                variant='default'
                disabled={updateEmailDomain.isPending}
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
              <p>{t('setting:emailDomain.help')}</p>
            </div>
          </div>
        )}

        {/* Confirmation Dialog */}
        <Dialog open={showConfirmDialog} onOpenChange={setShowConfirmDialog}>
          <DialogContent className='w-160 p-8'>
            <DialogHeader>
              <DialogTitle>{t('setting:emailDomain.confirmTitle')}</DialogTitle>
              <DialogDescription>
                {t('setting:emailDomain.confirmDescription', {
                  domain: tempDomain,
                })}
              </DialogDescription>
            </DialogHeader>
            <DialogFooter>
              <Button
                variant='outline'
                onClick={() => setShowConfirmDialog(false)}
              >
                {t('common:actions.cancel')}
              </Button>
              <Button
                onClick={handleSave}
                disabled={updateEmailDomain.isPending}
              >
                {updateEmailDomain.isPending ? (
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

export default EmailDomainSettings;
