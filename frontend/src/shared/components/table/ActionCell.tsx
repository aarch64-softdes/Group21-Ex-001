import { Button } from '@ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@ui/dropdown-menu';
import { ActionCellProps } from '@/core/types/table';

import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@ui/alert-dialog';
import { EllipsisVertical, Loader2 } from 'lucide-react';
import React from 'react';
import { useTranslation } from 'react-i18next';

const ActionCell: React.FC<ActionCellProps> = ({
  requireDeleteConfirmation = true,
  isDeleting = false,
  onView = () => {},
  onEdit = () => {},
  onDelete = () => {},
  disabledActions = {
    view: false,
    edit: false,
    delete: true,
  },
  additionalActions = [],
}) => {
  const { t } = useTranslation('common');
  const handleDeleteClick = () => {
    if (requireDeleteConfirmation) {
      setShowDeleteDialog(true);
    } else {
      onDelete?.();
    }
  };

  const [showDeleteDialog, setShowDeleteDialog] = React.useState(false);

  if (isDeleting) {
    return (
      <div className='flex p-1'>
        <Loader2 className='h-6 w-6 animate-spin' />
      </div>
    );
  }

  return (
    <>
      <DropdownMenu modal={false}>
        <DropdownMenuTrigger asChild>
          <Button
            className='h-8 w-8 rounded-full border-0 p-1 hover:bg-gray-200 data-[state=open]:bg-gray-200'
            variant={'outline'}
          >
            <EllipsisVertical />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent
          align='start'
          side='left'
          onCloseAutoFocus={(e) => e.preventDefault()}
        >
          <DropdownMenuLabel>{t('table.actions')}</DropdownMenuLabel>
          {!disabledActions.view && (
            <DropdownMenuItem onClick={onView}>
              {t('table.view')}
            </DropdownMenuItem>
          )}
          {!disabledActions.edit && (
            <>
              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={onEdit}>
                {t('table.edit')}
              </DropdownMenuItem>
            </>
          )}

          {additionalActions?.map((action, index) =>
            action.disabled ? null : (
              <React.Fragment key={`action-${index}`}>
                <DropdownMenuSeparator />
                <DropdownMenuItem
                  onClick={() => {
                    action.handler();
                  }}
                >
                  <span className='flex items-center gap-2'>
                    {action.label}
                  </span>
                </DropdownMenuItem>
              </React.Fragment>
            ),
          )}

          {!disabledActions.delete && (
            <>
              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={handleDeleteClick}>
                {t('table.delete')}
              </DropdownMenuItem>
            </>
          )}
        </DropdownMenuContent>
      </DropdownMenu>

      <AlertDialog open={showDeleteDialog} onOpenChange={setShowDeleteDialog}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>{t('messages.areYouSure')}</AlertDialogTitle>
            <AlertDialogDescription>
              {t('messages.confirmDelete')}
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>{t('actions.cancel')}</AlertDialogCancel>
            <AlertDialogAction
              onClick={() => {
                setShowDeleteDialog(false);
                onDelete?.();
              }}
              className='bg-red-600 hover:bg-red-700'
            >
              {t('actions.delete')}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
};

export default ActionCell;
