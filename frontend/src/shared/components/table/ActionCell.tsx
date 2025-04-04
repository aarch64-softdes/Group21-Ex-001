import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
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
} from '@/components/ui/alert-dialog';
import { EllipsisVertical, Loader2 } from 'lucide-react';
import React from 'react';

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
          <DropdownMenuLabel>Actions</DropdownMenuLabel>
          {!disabledActions.view && (
            <DropdownMenuItem onClick={onView}>View</DropdownMenuItem>
          )}
          {!disabledActions.edit && (
            <>
              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={onEdit}>Edit</DropdownMenuItem>
            </>
          )}
          {!disabledActions.delete && (
            <>
              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={handleDeleteClick}>
                Delete
              </DropdownMenuItem>
            </>
          )}
          {additionalActions?.map((action, index) => (
            <React.Fragment key={`action-${index}`}>
              <DropdownMenuSeparator />
              <DropdownMenuItem
                onClick={() => {
                  action.handler();
                }}
              >
                <span className='flex items-center gap-2'>{action.label}</span>
              </DropdownMenuItem>
            </React.Fragment>
          ))}
        </DropdownMenuContent>
      </DropdownMenu>

      <AlertDialog open={showDeleteDialog} onOpenChange={setShowDeleteDialog}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Are you sure?</AlertDialogTitle>
            <AlertDialogDescription>
              This action cannot be undone. This will permanently delete the
              record.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancel</AlertDialogCancel>
            <AlertDialogAction
              onClick={() => {
                setShowDeleteDialog(false);
                onDelete?.();
              }}
              className='bg-red-600 hover:bg-red-700'
            >
              Delete
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
};

export default ActionCell;
