import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import * as z from 'zod';
import { useTranslation } from 'react-i18next';

import { Button } from '@ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@ui/card';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@ui/form';
import { Input } from '@ui/input';
import { Separator } from '@ui/separator';
import { useStatus, useStatuses } from '@/features/status/api/useStatusApi';
import { FormComponentProps } from '@/core/types/table';
import { Loader2, ChevronsUpDown, Check, X } from 'lucide-react';
import { useEffect, useState } from 'react';
import LoadingButton from '@ui/loadingButton';
import { Popover, PopoverContent, PopoverTrigger } from '@ui/popover';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@ui/command';
import { ScrollArea } from '@ui/scroll-area';
import { Badge } from '@ui/badge';
import Status, { CreateStatusDTO, UpdateStatusDTO } from '@status/types/status';

// Define schema
const StatusFormSchema = (t: any) =>
  z.object({
    name: z
      .string()
      .min(1, t('validation.required'))
      .max(255, t('validation.maxLength', { length: 255 }))
      .regex(/^[\p{L}\s]*$/u, t('validation.nameFormat')),
    allowedTransitions: z
      .array(
        z
          .object({
            id: z.string(),
          })
          .optional(),
      )
      .optional(),
  });

export type StatusFormValues = z.infer<ReturnType<typeof StatusFormSchema>>;

const StatusForm: React.FC<FormComponentProps<Status>> = ({
  onSubmit,
  onCancel,
  id,
  isLoading = false,
  isEditing = false,
}) => {
  const { t } = useTranslation(['status', 'common']);

  const { data: statusData, isLoading: isLoadingStatus } = useStatus(id || '');
  const { data: statusesData, isLoading: isLoadingStatuses } = useStatuses({
    page: 1,
    pageSize: 100,
    filters: {},
    sort: {
      key: 'name',
      direction: 'asc',
    },
  });

  const [open, setOpen] = useState(false);
  const [selectedStatuses, setSelectedStatuses] = useState<Status[]>([]);

  const form = useForm<StatusFormValues>({
    resolver: zodResolver(StatusFormSchema(t)),
    defaultValues: {
      name: '',
      allowedTransitions: [],
    },
  });

  useEffect(() => {
    if (statusData && id) {
      form.reset({
        name: statusData.name || '',
        allowedTransitions: statusData.allowedTransitions || [],
      });

      if (statusData.allowedTransitions) {
        setSelectedStatuses(statusData.allowedTransitions);
      }
    }
  }, [statusData, id, form]);

  const handleSubmit = (values: StatusFormValues) => {
    if (isEditing) {
      onSubmit(values as UpdateStatusDTO);
    } else {
      onSubmit(values as CreateStatusDTO);
    }
  };

  // Add a status to allowedTransitions
  const addStatus = (status: Status) => {
    const currentTransitions = form.getValues('allowedTransitions') || [];

    if (id && id == status.id) {
      return;
    }

    // Prevent duplicates
    if (!currentTransitions.some((s) => s?.id === status.id)) {
      const newTransitions = [...currentTransitions, status];
      form.setValue('allowedTransitions', newTransitions);

      const newSelectedStatuses = [...selectedStatuses, status];
      setSelectedStatuses(newSelectedStatuses);
    }
  };

  // Remove a status from allowedTransitions
  const removeStatus = (statusId: string) => {
    if (!statusId) {
      return;
    }

    const currentTransitions = form.getValues('allowedTransitions') || [];
    const newTransitions = currentTransitions.filter(
      (status) => status?.id !== statusId,
    );
    form.setValue('allowedTransitions', newTransitions);

    const newSelectedStatuses = selectedStatuses.filter(
      (status) => status.id !== statusId,
    );
    setSelectedStatuses(newSelectedStatuses);
  };

  // Filter out the current status from available transitions
  const availableStatuses =
    statusesData?.data?.filter((status) => !id || id !== status.id) || [];

  // Determine if we should show loading state
  const isFormLoading = isLoading || (isEditing && isLoadingStatus);

  return (
    <div className='sticky inset-0'>
      {/* Header */}
      <div className='border-b px-4 py-2 flex items-center justify-between'>
        <h2 className='text-xl font-semibold'>
          {isEditing ? t('status:editStatus') : t('status:addNew')}
        </h2>
      </div>

      {/* Content */}
      <div className='flex-1 overflow-y-auto p-4 min-h-0'>
        <div className='max-w-5xl mx-auto pb-4'>
          <Form {...form}>
            {isFormLoading ? (
              <div className='flex items-center justify-center p-6'>
                <Loader2 className='h-8 w-8 animate-spin' />
              </div>
            ) : (
              <form
                onSubmit={form.handleSubmit(handleSubmit)}
                className='max-w-5xl mx-auto'
                autoComplete='off'
              >
                <Card className='mb-6'>
                  <CardHeader>
                    <CardTitle className='text-lg font-medium'>
                      {t('status:sections.statusInfo')}
                    </CardTitle>
                    <Separator />
                  </CardHeader>
                  <CardContent>
                    <FormField
                      control={form.control}
                      name='name'
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>{t('status:fields.name')}</FormLabel>
                          <FormControl>
                            <Input
                              placeholder={t(
                                'status:fields.namePlaceholder',
                                'Enter status name',
                              )}
                              {...field}
                              autoComplete='off'
                              onKeyDown={(e) => {
                                if (e.key === 'Enter') {
                                  e.preventDefault();
                                }
                              }}
                            />
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </CardContent>
                </Card>

                <Card className='mb-6'>
                  <CardHeader>
                    <CardTitle className='text-lg font-medium'>
                      {t('status:sections.transitions')}
                    </CardTitle>
                    <Separator />
                  </CardHeader>
                  <CardContent>
                    <FormField
                      control={form.control}
                      name='allowedTransitions'
                      render={() => (
                        <FormItem>
                          <div className='flex flex-col gap-4'>
                            <div className='flex flex-wrap gap-2 mb-2'>
                              {selectedStatuses.map((status) => (
                                <Badge
                                  key={status.id}
                                  variant='secondary'
                                  className='flex items-center gap-1'
                                  onClick={() => removeStatus(status.id)}
                                >
                                  {status.name}
                                  <button
                                    type='button'
                                    className='text-xs text-muted-foreground px-1 cursor-pointer'
                                    onClick={() => {
                                      removeStatus(status.id);
                                    }}
                                  >
                                    <X
                                      className='h-4 w-4'
                                      aria-label={t('status:actions.remove', {
                                        name: status.name,
                                      })}
                                    />
                                  </button>
                                </Badge>
                              ))}
                              {selectedStatuses.length === 0 && (
                                <span className='text-sm flex items-center text-gray-500'>
                                  {t('status:messages.noTransitions')}
                                </span>
                              )}

                              <Popover open={open} onOpenChange={setOpen}>
                                <PopoverTrigger asChild>
                                  <Button
                                    variant='outline'
                                    role='combobox'
                                    aria-expanded={open}
                                    className=' justify-between'
                                    disabled={isLoadingStatuses}
                                  >
                                    {isLoadingStatuses ? (
                                      <Loader2 className='h-4 w-4 animate-spin' />
                                    ) : (
                                      <>
                                        {t('status:messages.selectStatus')}
                                        <ChevronsUpDown className='ml-auto h-4 w-4 shrink-0 opacity-50' />
                                      </>
                                    )}
                                  </Button>
                                </PopoverTrigger>
                                <PopoverContent className='w-[300px] p-0'>
                                  <Command>
                                    <CommandInput
                                      placeholder={t(
                                        'status:fields.searchPlaceholder',
                                        'Search status...',
                                      )}
                                    />
                                    <CommandEmpty>
                                      {t('common:messages.noData')}
                                    </CommandEmpty>
                                    <CommandList>
                                      <ScrollArea className='max-h-[200px] min-h-full'>
                                        <CommandGroup>
                                          {availableStatuses.map((status) => (
                                            <CommandItem
                                              key={status.id}
                                              value={status.name}
                                              onSelect={() => {
                                                addStatus(status);
                                                setOpen(false);
                                              }}
                                              className='flex items-center gap-2'
                                            >
                                              {status.name}
                                              {selectedStatuses.some(
                                                (s) => s.id === status.id,
                                              ) && (
                                                <Check className='ml-auto h-4 w-4' />
                                              )}
                                            </CommandItem>
                                          ))}
                                        </CommandGroup>
                                      </ScrollArea>
                                    </CommandList>
                                  </Command>
                                </PopoverContent>
                              </Popover>
                            </div>
                          </div>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </CardContent>
                </Card>

                <div className='flex justify-end gap-2 pt-4'>
                  <Button
                    type='button'
                    variant='outline'
                    onClick={() => {
                      form.reset();
                      setSelectedStatuses([]);
                      onCancel();
                    }}
                  >
                    {t('common:actions.cancel')}
                  </Button>
                  <LoadingButton type='submit' isLoading={isLoading}>
                    {isEditing
                      ? t('common:actions.save')
                      : t('common:actions.add')}
                  </LoadingButton>
                </div>
              </form>
            )}
          </Form>
        </div>
      </div>
    </div>
  );
};

export default StatusForm;
