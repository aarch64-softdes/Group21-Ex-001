import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import * as z from "zod";

import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import Student, {
  Gender,
  Faculty,
  Status,
  CreateStudentDTO,
} from "@/types/student";
import {
  useFaculties,
  useGenders,
  useStudentStatuses,
} from "@/hooks/useMetadata";
import { Loader2 } from "lucide-react";
import { useEffect } from "react";
import { FormComponentProps } from "@/types/table";
import LoadingButton from "../ui/loadingButton";
import { useStudent } from "@/hooks/useStudentApi";

// Define schema based on the Java validation annotations
export const StudentFormSchema = z.object({
  studentId: z.string().min(1, "Student ID is required"),
  name: z
    .string()
    .min(1, "Name is required")
    .regex(/^[a-zA-Z\s]*$/, "Name must contain only letters"),
  dob: z.string().refine((date) => {
    const today = new Date();
    const birthDate = new Date(date);
    return birthDate < today;
  }, "Date of birth must be in the past"),
  gender: z.string().min(1, "Gender is required"),
  faculty: z.string().min(1, "Faculty is required"),
  course: z.number().int().positive(),
  program: z.string(),
  email: z.string().email("Invalid email address"),
  address: z.string().optional(),
  phone: z
    .string()
    .regex(/^0\d{9}$/, "Phone number must start with 0 and have 10 digits"),
  status: z.string().min(1, "Status is required").default("Studying"),
});

export type StudentFormValues = z.infer<typeof StudentFormSchema>;

const StudentForm: React.FC<FormComponentProps<Student>> = ({
  onSubmit,
  onCancel,
  id,
  isLoading = false,
  isEditing = false,
}) => {
  const gendersQuery = useGenders();
  const facultiesQuery = useFaculties();
  const statusesQuery = useStudentStatuses();
  const { data: studentData, isLoading: isLoadingStudent } = useStudent(
    id ?? ""
  );

  const form = useForm<StudentFormValues>({
    resolver: zodResolver(StudentFormSchema),
    defaultValues: {
      studentId: "",
      name: "",
      dob: "",
      gender: "",
      faculty: "",
      course: 1,
      program: "",
      email: "",
      phone: "",
      status: "Studying",
    },
  });

  const formatDateForInput = (date: Date | string): string => {
    if (!date) return "";

    const d = typeof date === "string" ? new Date(date) : date;
    return d.toISOString().split("T")[0]; // Returns YYYY-MM-DD
  };

  // Reset form when student data is loaded
  useEffect(() => {
    if (studentData && id) {
      const formattedDob =
        studentData.dob instanceof Date
          ? formatDateForInput(studentData.dob)
          : formatDateForInput(studentData.dob as string);
      console.log(studentData.dob, formattedDob);

      form.reset({
        studentId: studentData.studentId || id,
        name: studentData.name || "",
        dob: formattedDob || "",
        gender: studentData.gender || "",
        faculty: studentData.faculty || "",
        course: parseInt(studentData.course?.toString() || "1", 10),
        program: studentData.program || "",
        email: studentData.email || "",
        phone: studentData.phone || "",
        address: studentData.address || "",
        status: studentData.status || "Studying",
      });
    }
  }, [studentData, id, form]);

  const handleSubmit = (values: StudentFormValues) => {
    console.log("Submitting form with values:", values);

    const formattedValues = {
      ...values,
      dob: new Date(values.dob),
    };

    onSubmit(formattedValues as unknown as CreateStudentDTO);
  };

  // Determine if we should show loading state
  const isFormLoading = isLoading || (isEditing && isLoadingStudent);

  return (
    <Form {...form}>
      <h2 className="mb-4 text-xl font-semibold">
        {isEditing ? "Edit Student" : "Add New Student"}
      </h2>
      {isFormLoading ? (
        <div className="flex items-center justify-center p-6">
          <Loader2 className="h-8 w-8 animate-spin" />
        </div>
      ) : (
        <form
          onSubmit={form.handleSubmit(handleSubmit)}
          className="mx-auto w-full max-w-md space-y-4 p-6"
          autoComplete="off"
          noValidate
        >
          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="studentId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Student ID</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="e.g. S12345"
                      {...field}
                      disabled={isEditing}
                      autoComplete="off"
                      onKeyDown={(e) => {
                        if (e.key === "Enter") {
                          e.preventDefault();
                        }
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Name</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="John Doe"
                      {...field}
                      autoComplete="off"
                      onKeyDown={(e) => {
                        if (e.key === "Enter") {
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

          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="dob"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Date of Birth</FormLabel>
                  <FormControl>
                    <Input
                      type="date"
                      {...field}
                      autoComplete="off"
                      onKeyDown={(e) => {
                        if (e.key === "Enter") {
                          e.preventDefault();
                        }
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="gender"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Gender</FormLabel>
                  <Select
                    onValueChange={field.onChange}
                    value={field.value}
                    defaultValue={field.value}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select gender" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {gendersQuery.isLoading ? (
                        <div className="flex items-center justify-center p-2">
                          <Loader2 className="h-4 w-4 animate-spin" />
                        </div>
                      ) : gendersQuery.data ? (
                        gendersQuery.data.map((gender) => (
                          <SelectItem key={gender} value={gender}>
                            {gender.replace(/_/g, " ")}
                          </SelectItem>
                        ))
                      ) : (
                        <SelectItem value="" disabled>
                          Failed to load genders
                        </SelectItem>
                      )}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="faculty"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Faculty</FormLabel>
                  <Select
                    onValueChange={field.onChange}
                    value={field.value}
                    defaultValue={field.value}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select faculty" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {facultiesQuery.isLoading ? (
                        <div className="flex items-center justify-center p-2">
                          <Loader2 className="h-4 w-4 animate-spin" />
                        </div>
                      ) : facultiesQuery.data ? (
                        facultiesQuery.data.map((faculty) => (
                          <SelectItem key={faculty.value} value={faculty.value}>
                            {faculty.value}
                          </SelectItem>
                        ))
                      ) : (
                        <SelectItem value="" disabled>
                          Failed to load faculties
                        </SelectItem>
                      )}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="course"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Course Year</FormLabel>
                  <FormControl>
                    <Input
                      type="number"
                      min="1"
                      max="6"
                      {...field}
                      onChange={(e) =>
                        field.onChange(parseInt(e.target.value, 10))
                      }
                      autoComplete="off"
                      onKeyDown={(e) => {
                        if (e.key === "Enter") {
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

          <FormField
            control={form.control}
            name="program"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Program</FormLabel>
                <FormControl>
                  <Input
                    placeholder="e.g. Computer Science"
                    {...field}
                    autoComplete="off"
                    onKeyDown={(e) => {
                      if (e.key === "Enter") {
                        e.preventDefault();
                      }
                    }}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Email</FormLabel>
                <FormControl>
                  <Input
                    placeholder="example@email.com"
                    {...field}
                    autoComplete="off"
                    onKeyDown={(e) => {
                      if (e.key === "Enter") {
                        e.preventDefault();
                      }
                    }}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="address"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Address</FormLabel>
                <FormControl>
                  <Input
                    placeholder="123 Main St, City"
                    {...field}
                    autoComplete="off"
                    onKeyDown={(e) => {
                      if (e.key === "Enter") {
                        e.preventDefault();
                      }
                    }}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="phone"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Phone</FormLabel>
                <FormControl>
                  <Input
                    placeholder="0123456789"
                    {...field}
                    autoComplete="off"
                    onKeyDown={(e) => {
                      if (e.key === "Enter") {
                        e.preventDefault();
                      }
                    }}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="status"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Status</FormLabel>
                <Select
                  onValueChange={field.onChange}
                  value={field.value}
                  defaultValue={field.value}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select status" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    {statusesQuery.isLoading ? (
                      <div className="flex items-center justify-center p-2">
                        <Loader2 className="h-4 w-4 animate-spin" />
                      </div>
                    ) : statusesQuery.data ? (
                      statusesQuery.data.map((status) => (
                        <SelectItem key={status} value={status}>
                          {status.replace(/_/g, " ")}
                        </SelectItem>
                      ))
                    ) : (
                      <SelectItem value="" disabled>
                        Failed to load statuses
                      </SelectItem>
                    )}
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />

          <div className="flex justify-end gap-2 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => {
                form.reset();
                onCancel();
              }}
            >
              Cancel
            </Button>
            <LoadingButton type="submit" isLoading={isLoading}>
              {isEditing ? "Save Changes" : "Add Student"}
            </LoadingButton>
          </div>
        </form>
      )}
    </Form>
  );
};

export default StudentForm;
