import { Button } from '@ui/button';
import { useTranslation } from 'react-i18next';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@ui/command';
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@ui/form';
import { Input } from '@ui/input';
import { Popover, PopoverContent, PopoverTrigger } from '@ui/popover';
import { ScrollArea } from '@ui/scroll-area';
import { usePhoneSetting } from '@/features/settings/api/useSettingsApi';
import {
  Country,
  countries,
  findCountryByCode,
} from '@/shared/data/countryData';
import { Check, ChevronsUpDown } from 'lucide-react';
import { useEffect, useState } from 'react';
import { UseFormReturn } from 'react-hook-form';
import { t } from 'i18next';

interface PhoneFieldProps {
  form: UseFormReturn<any>;
}

const PhoneField = ({ form }: PhoneFieldProps) => {
  const { data: supportedCountryCodes, isLoading } = usePhoneSetting();
  const [open, setOpen] = useState(false);
  const [availableCountries, setAvailableCountries] = useState<Country[]>([]);

  // Convert API country codes to Country objects
  useEffect(() => {
    if (supportedCountryCodes) {
      const countryCodes = supportedCountryCodes;
      const filteredCountries = countryCodes
        .map((code: string) => findCountryByCode(code))
        .filter((c: Country | undefined) => c !== undefined) as Country[];

      setAvailableCountries(
        filteredCountries.length > 0 ? filteredCountries : countries,
      );
    } else {
      // Default to all countries if no settings are found
      setAvailableCountries(countries);
    }
  }, [supportedCountryCodes]);

  // Get current country code value
  const countryCode = form.watch('phone.countryCode') || 'VN';
  const selectedCountry =
    findCountryByCode(countryCode) || countries.find((c) => c.code === 'VN');

  return (
    <div className='space-y-2'>
      <FormLabel className='block'>{t('student:fields.phone.label')}</FormLabel>
      <div className='flex gap-2'>
        {/* Country Code Selector */}
        <Popover open={open} onOpenChange={setOpen}>
          <PopoverTrigger asChild>
            <Button
              variant='outline'
              role='combobox'
              aria-expanded={open}
              className='w-[140px] justify-between'
            >
              {selectedCountry ? (
                <>
                  <span className='mr-1'>{selectedCountry.flag}</span>
                  <span>{selectedCountry.dialCode}</span>
                </>
              ) : (
                t('student:fields.phone.selectCode', 'Select code')
              )}
              <ChevronsUpDown className='ml-auto h-4 w-4 shrink-0 opacity-50' />
            </Button>
          </PopoverTrigger>
          <PopoverContent className='w-[250px] p-0'>
            <Command>
              <CommandInput
                placeholder={t(
                  'student:fields.phone.searchCountry',
                  'Search country...',
                )}
              />
              <CommandEmpty>{t('common:messages.noData')}</CommandEmpty>
              <CommandList>
                <ScrollArea className='max-h-[120px] min-h-full'>
                  <CommandGroup>
                    {availableCountries.map((country) => (
                      <CommandItem
                        key={country.code}
                        value={`${country.name}${country.dialCode}`}
                        onSelect={() => {
                          form.setValue('phone.countryCode', country.code);
                          setOpen(false);
                        }}
                        className='flex items-center gap-2'
                      >
                        <span className='mr-1'>{country.flag}</span>
                        {country.name} ({country.dialCode})
                        {country.code === countryCode && (
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

        {/* Phone Number Input */}
        <FormField
          control={form.control}
          name='phone.phoneNumber'
          render={({ field }) => (
            <FormItem className='flex-1'>
              <FormControl>
                <Input
                  {...field}
                  placeholder={t(
                    'student:fields.phone.placeholder',
                    '0123456789',
                  )}
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
      </div>
    </div>
  );
};

export default PhoneField;
