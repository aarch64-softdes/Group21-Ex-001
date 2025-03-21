import { FC } from "react";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { UseFormReturn } from "react-hook-form";
import { StudentFormValues } from "./StudentForm";

interface AddressFormProps {
  form: UseFormReturn<StudentFormValues>;
  type: "permanentAddress" | "temporaryAddress" | "mailingAddress";
  title: string;
}

const AddressForm: FC<AddressFormProps> = ({ form, type, title }) => {
  return (
    <fieldset className="border rounded-md p-4">
      <legend className="px-2 font-medium">{title}</legend>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <FormField
          control={form.control}
          name={`${type}.street`}
          render={({ field }) => (
            <FormItem className="col-span-2">
              <FormLabel>Street Address</FormLabel>
              <FormControl>
                <Input
                  placeholder="Street name, house number"
                  {...field}
                  value={field.value || ""}
                  autoComplete="off"
                  onKeyDown={(e) => {
                    if (e.key === "Enter") e.preventDefault();
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
              <FormLabel>Country</FormLabel>
              <FormControl>
                <Input
                  placeholder="Country"
                  {...field}
                  value={field.value || "Việt Nam"}
                  autoComplete="off"
                  onKeyDown={(e) => {
                    if (e.key === "Enter") e.preventDefault();
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
              <FormLabel>Ward</FormLabel>
              <FormControl>
                <Input
                  placeholder="Ward/Commune"
                  {...field}
                  value={field.value || ""}
                  autoComplete="off"
                  onKeyDown={(e) => {
                    if (e.key === "Enter") e.preventDefault();
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
              <FormLabel>District</FormLabel>
              <FormControl>
                <Input
                  placeholder="District"
                  {...field}
                  value={field.value || ""}
                  autoComplete="off"
                  onKeyDown={(e) => {
                    if (e.key === "Enter") e.preventDefault();
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
              <FormLabel>City/Province</FormLabel>
              <FormControl>
                <Input
                  placeholder="City/Province"
                  {...field}
                  value={field.value || ""}
                  autoComplete="off"
                  onKeyDown={(e) => {
                    if (e.key === "Enter") e.preventDefault();
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
