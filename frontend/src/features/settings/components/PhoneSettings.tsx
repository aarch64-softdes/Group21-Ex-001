import { Button } from '@ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@ui/card';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@ui/dialog';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@ui/command';
import { Popover, PopoverContent, PopoverTrigger } from '@ui/popover';
import { Badge } from '@ui/badge';
import { ScrollArea } from '@ui/scroll-area';
import { Check, ChevronsUpDown, Loader2, Pencil, Save, X } from 'lucide-react';
import {
  usePhoneSetting,
  useUpdatePhoneSetting,
} from '@/features/settings/api/useSettingsApi';
import {
  Country,
  countries,
  findCountryByCode,
} from '@/shared/data/countryData';
import React, { useEffect, useState } from 'react';
import { cn, getErrorMessage } from '@/shared/lib/utils';
import { showErrorToast, showSuccessToast } from '@/shared/lib/toast-utils';
import { useTranslation } from 'react-i18next';

const PhoneSettings: React.FC<{ className?: string }> = ({ className }) => {
  const { t } = useTranslation(['setting', 'common']);
  const { data, isLoading } = usePhoneSetting();
  const updatePhoneSetting = useUpdatePhoneSetting();

  const [isEditing, setIsEditing] = useState(false);
  const [selectedCountries, setSelectedCountries] = useState<Country[]>([]);
  const [open, setOpen] = useState(false);
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);

  // When data is loaded, set the selected countries
  useEffect(() => {
    if (data && !isEditing) {
      const countriesFromCodes = data
        .map((code) => findCountryByCode(code))
        .filter((country): country is Country => !!country);

      setSelectedCountries(countriesFromCodes);
    }
  }, [data, isEditing]);

  const handleEditToggle = () => {
    if (isEditing) {
      // Show confirmation dialog
      setShowConfirmDialog(true);
    } else {
      setIsEditing(true);
    }
  };

  const handleSave = async () => {
    setShowConfirmDialog(false);

    try {
      const codes = selectedCountries.map((country) => country.code);
      await updatePhoneSetting.mutateAsync({
        supportedCountryCodes: codes,
      });
      setIsEditing(false);
      showSuccessToast(
        t('setting:messages.updated', {
          setting: t('setting:phoneNumber.title'),
        }),
      );
    } catch (err) {
      console.error('Failed to update phone settings:', err);
      showErrorToast(
        t('setting:messages.updateFailed', {
          setting: t('setting:phoneNumber.title'),
          error: getErrorMessage(err),
        }),
      );
    }
  };

  const handleCancel = () => {
    if (data) {
      const countriesFromCodes = data
        .map((code) => findCountryByCode(code))
        .filter((country): country is Country => !!country);

      setSelectedCountries(countriesFromCodes);
    }
    setIsEditing(false);
  };

  const toggleCountry = (country: Country) => {
    setSelectedCountries((current) => {
      // Check if country is already selected
      const isSelected = current.some((c) => c.code === country.code);

      if (isSelected) {
        // Remove country
        return current.filter((c) => c.code !== country.code);
      } else {
        // Add country
        return [...current, country];
      }
    });
  };

  const removeCountry = (country: Country) => {
    setSelectedCountries((current) =>
      current.filter((c) => c.code !== country.code),
    );
  };

  return (
    <Card className={cn('w-160', className)}>
      <CardHeader>
        <CardTitle>{t('setting:phoneNumber.title')}</CardTitle>
        <CardDescription>
          {t('setting:phoneNumber.description')}
        </CardDescription>
      </CardHeader>
      <CardContent>
        {isLoading ? (
          <div className='flex items-center justify-center p-4'>
            <Loader2 className='h-8 w-8 animate-spin' />
          </div>
        ) : (
          <div className='space-y-4'>
            <div className='flex flex-col space-y-2'>
              <div className='flex justify-between items-center'>
                <label className='text-sm font-medium text-gray-700 mb-1'>
                  {t('setting:phoneNumber.label')}
                </label>
                <div className='flex gap-2'>
                  {isEditing && (
                    <Button variant='outline' onClick={handleCancel}>
                      {t('setting:actions.cancel')}
                    </Button>
                  )}
                  <Button
                    onClick={handleEditToggle}
                    variant='default'
                    disabled={updatePhoneSetting.isPending}
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
                </div>
              </div>

              {/* Display selected countries */}
              <div className='flex flex-wrap gap-2 mb-2 min-h-10'>
                {selectedCountries.length === 0 ? (
                  <p className='text-sm text-gray-500'>
                    {t('setting:phoneNumber.noCountry')}
                  </p>
                ) : (
                  selectedCountries.map((country) => (
                    <Badge
                      key={country.code}
                      variant='outline'
                      className='flex items-center gap-1'
                    >
                      <span className='mr-1'>{country.flag}</span>
                      {country.name} ({country.dialCode})
                      {isEditing && (
                        <button
                          onClick={() => removeCountry(country)}
                          className='ml-1 text-gray-500 hover:text-gray-700'
                        >
                          <X className='h-3 w-3' />
                        </button>
                      )}
                    </Badge>
                  ))
                )}
              </div>

              {/* Country selector dropdown */}
              {isEditing && (
                <Popover open={open} onOpenChange={setOpen}>
                  <PopoverTrigger asChild>
                    <Button
                      variant='outline'
                      role='combobox'
                      aria-expanded={open}
                      className='justify-between w-full'
                    >
                      {t('setting:phoneNumber.selectCountry')}
                      <ChevronsUpDown className='ml-2 h-4 w-4 shrink-0 opacity-50' />
                    </Button>
                  </PopoverTrigger>
                  <PopoverContent className='w-full p-0'>
                    <Command>
                      <CommandInput
                        placeholder={t('setting:phoneNumber.searchCountry')}
                      />
                      <CommandEmpty>{t('common:messages.noData')}</CommandEmpty>
                      <CommandList>
                        <ScrollArea className='h-60'>
                          <CommandGroup>
                            {countries.map((country) => {
                              const isSelected = selectedCountries.some(
                                (c) => c.code === country.code,
                              );
                              return (
                                <CommandItem
                                  key={country.code}
                                  value={`${country.name}${country.dialCode}`}
                                  onSelect={() => toggleCountry(country)}
                                  className={cn(
                                    'flex items-center gap-2',
                                    isSelected ? 'bg-accent' : '',
                                  )}
                                >
                                  <span className='mr-1'>{country.flag}</span>
                                  {country.name} ({country.dialCode})
                                  {isSelected && (
                                    <Check className='ml-auto h-4 w-4' />
                                  )}
                                </CommandItem>
                              );
                            })}
                          </CommandGroup>
                        </ScrollArea>
                      </CommandList>
                    </Command>
                  </PopoverContent>
                </Popover>
              )}
            </div>
            <div className='text-sm text-gray-500'>
              <p>{t('setting:phoneNumber.help')}</p>
            </div>
          </div>
        )}

        {/* Confirmation Dialog */}
        <Dialog open={showConfirmDialog} onOpenChange={setShowConfirmDialog}>
          <DialogContent className='w-160 p-8'>
            <DialogHeader>
              <DialogTitle>{t('setting:phoneNumber.confirmTitle')}</DialogTitle>
              <DialogDescription>
                {t('setting:phoneNumber.confirmDescription')}
              </DialogDescription>
            </DialogHeader>
            <div className='py-4'>
              <h4 className='text-sm font-medium mb-2'>
                {t('setting:phoneNumber.selectedCountries')}
              </h4>
              <div className='flex flex-wrap gap-2'>
                {selectedCountries.length === 0 ? (
                  <p className='text-sm text-gray-500'>
                    {t('setting:phoneNumber.noCountry')}
                  </p>
                ) : (
                  selectedCountries.map((country) => (
                    <Badge key={country.code} variant='outline'>
                      <span className='mr-1'>{country.flag}</span>
                      {country.name} ({country.dialCode})
                    </Badge>
                  ))
                )}
              </div>
            </div>
            <DialogFooter>
              <Button
                variant='outline'
                onClick={() => setShowConfirmDialog(false)}
              >
                {t('common:actions.cancel')}
              </Button>
              <Button
                onClick={handleSave}
                disabled={updatePhoneSetting.isPending}
              >
                {updatePhoneSetting.isPending ? (
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

export default PhoneSettings;
