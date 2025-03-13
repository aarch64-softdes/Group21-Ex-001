import React from "react";
import { DetailComponentProps } from "@/types/table";
import { useStudent } from "@/hooks/useStudentApi";

const StudentDetail: React.FC<DetailComponentProps> = ({ id: studentId }) => {
  const { data: student } = useStudent(studentId as string);

  if (!student) {
    return <></>;
  }

  const formatDate = (dateString: string | Date) => {
    const date = new Date(dateString);
    return date.toLocaleDateString();
  };

  const detailItems = [
    { label: "Student ID", value: student.studentId },
    { label: "Name", value: student.name },
    { label: "Date of Birth", value: formatDate(student.dob) },
    { label: "Gender", value: student.gender },
    { label: "Faculty", value: student.faculty },
    { label: "Program", value: student.program },
    { label: "Course Year", value: student.course },
    { label: "Email", value: student.email },
    { label: "Phone", value: student.phone },
    { label: "Address", value: student.address || "N/A" },
    { label: "Status", value: student.status },
  ];

  return (
    <div className="mt-4 space-y-4">
      {detailItems.map((item, index) => (
        <div key={index} className="grid grid-cols-3 gap-4">
          <div className="font-medium text-gray-500">{item.label}:</div>
          <div className="col-span-2">{item.value}</div>
        </div>
      ))}
    </div>
  );
};

export default StudentDetail;
