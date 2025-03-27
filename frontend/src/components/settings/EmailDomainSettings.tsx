import { Button } from '@/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import {
  useEmailDomainSetting,
  useUpdateEmailDomainSetting,
} from '@/hooks/api/useSettingsApi';
import { useState } from 'react';
import { Pencil, Save } from 'lucide-react';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Loader2 } from 'lucide-react';
import { cn, getErrorMessage } from '@/lib/utils';
import { showErrorToast, showSuccessToast } from '@/lib/toast-utils';

const validateEmailDomain = (domain: string): string | null => {
  if (!domain.startsWith('@')) {
    return 'Domain must start with @';
  }

  // Simple regex to validate domain format after the @
  const domainRegex =
    /^@[a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9](?:\.[a-zA-Z]{2,})+$/;
  if (!domainRegex.test(domain)) {
    return 'Invalid domain format';
  }

  return null;
};

const EmailDomainSettings: React.FC<{ className?: string }> = (className) => {
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
      showSuccessToast('Email domain updated successfully');
    } catch (err) {
      console.error('Failed to update email domain:', err);
      showErrorToast('Failed to update email domain: ' + getErrorMessage(err));
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
        <CardTitle>Email Domain Setting</CardTitle>
        <CardDescription>
          Configure the allowed email domain for all students
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
                  Email Domain
                </label>
                <Input
                  id='emailDomain'
                  value={tempDomain}
                  onChange={handleInputChange}
                  disabled={!isEditing}
                  placeholder='@example.com'
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
                This domain will be enforced for all student email addresses.
              </p>
            </div>
          </div>
        )}

        {/* Confirmation Dialog */}
        <Dialog open={showConfirmDialog} onOpenChange={setShowConfirmDialog}>
          <DialogContent className='w-160 p-8'>
            <DialogHeader>
              <DialogTitle>Confirm Email Domain Change</DialogTitle>
              <DialogDescription>
                You are about to change the email domain to{' '}
                <strong>{tempDomain}</strong>. This will affect all new student
                registrations. Are you sure?
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
                disabled={updateEmailDomain.isPending}
              >
                {updateEmailDomain.isPending ? (
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

export default EmailDomainSettings;
