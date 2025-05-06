import { FC } from 'react';
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@ui/form';
import { Input } from '@ui/input';
import { UseFormReturn } from 'react-hook-form';
import { StudentFormValues } from '@student/components/StudentForm';
import { t } from 'i18next';

interface AddressFormProps {
  form: UseFormReturn<StudentFormValues>;
  type: 'permanentAddress' | 'temporaryAddress' | 'mailingAddress';
  title: string;
}

const AddressForm: FC<AddressFormProps> = ({ form, type, title }) => {
  return (
    <fieldset className='border rounded-md p-4'>
      <legend className='px-2 font-medium'>{title}</legend>
      <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4'>
        <FormField
          control={form.control}
          name={`${type}.street`}
          render={({ field }) => (
            <FormItem className='col-span-2'>
              <FormLabel>{t('student:fields.address.street')}</FormLabel>
              <FormControl>
                <Input
                  placeholder={t(
                    'student:fields.address.streetPlaceholder',
                    'Street name, house number',
                  )}
                  {...field}
                  value={field.value || ''}
                  autoComplete='off'
                  onKeyDown={(e) => {
                    if (e.key === 'Enter') e.preventDefault();
                  }}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name={`${type}.country`}
          render={({ field }) => (
            <FormItem>
              <FormLabel>{t('student:fields.address.country')}</FormLabel>
              <FormControl>
                <Input
                  placeholder={t(
                    'student:fields.address.countryPlaceholder',
                    'Country',
                  )}
                  {...field}
                  value={field.value || 'Việt Nam'}
                  autoComplete='off'
                  onKeyDown={(e) => {
                    if (e.key === 'Enter') e.preventDefault();
                  }}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name={`${type}.ward`}
          render={({ field }) => (
            <FormItem>
              <FormLabel>{t('student:fields.address.ward')}</FormLabel>
              <FormControl>
                <Input
                  placeholder={t(
                    'student:fields.address.wardPlaceholder',
                    'Ward/Commune',
                  )}
                  {...field}
                  value={field.value || ''}
                  autoComplete='off'
                  onKeyDown={(e) => {
                    if (e.key === 'Enter') e.preventDefault();
                  }}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name={`${type}.district`}
          render={({ field }) => (
            <FormItem>
              <FormLabel>{t('student:fields.address.district')}</FormLabel>
              <FormControl>
                <Input
                  placeholder={t(
                    'student:fields.address.districtPlaceholder',
                    'District',
                  )}
                  {...field}
                  value={field.value || ''}
                  autoComplete='off'
                  onKeyDown={(e) => {
                    if (e.key === 'Enter') e.preventDefault();
                  }}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name={`${type}.province`}
          render={({ field }) => (
            <FormItem>
              <FormLabel>{t('student:fields.address.province')}</FormLabel>
              <FormControl>
                <Input
                  placeholder={t(
                    'student:fields.address.provincePlaceholder',
                    'City/Province',
                  )}
                  {...field}
                  value={field.value || ''}
                  autoComplete='off'
                  onKeyDown={(e) => {
                    if (e.key === 'Enter') e.preventDefault();
                  }}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>
    </fieldset>
  );
};

export default AddressForm;
