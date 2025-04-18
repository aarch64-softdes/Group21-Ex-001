import React, { useState } from 'react';
import { useEnrollmentHistory } from '../api/useEnrollmentApi';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@ui/table';
import { Loader2 } from 'lucide-react';
import TablePagination from '@/components/table/TablePagination';
import { Badge } from '@ui/badge';

interface EnrollmentHistoryTableProps {
  studentId: string;
}

const EnrollmentHistoryTable: React.FC<EnrollmentHistoryTableProps> = ({
  studentId,
}) => {
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  const { data: historyResponse, isLoading } = useEnrollmentHistory(
    studentId,
    page,
    pageSize,
  );

  if (isLoading) {
    return (
      <div className='flex items-center justify-center h-48'>
        <Loader2 className='h-8 w-8 animate-spin text-primary' />
      </div>
    );
  }

  if (!historyResponse?.data || historyResponse.data.length === 0) {
    return (
      <div className='bg-white rounded-md p-6 text-center'>
        <p className='text-muted-foreground'>No enrollment history found.</p>
      </div>
    );
  }

  const getActionBadge = (actionType: string) => {
    switch (actionType.toLowerCase()) {
      case 'enrolled':
        return <Badge variant='default'>Enrolled</Badge>;
      case 'deleted':
        return <Badge variant='destructive'>Unenrolled</Badge>;
      default:
        return <Badge variant='secondary'>{actionType}</Badge>;
    }
  };

  const formatDate = (date: Date) => {
    return new Date(date).toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <div className='bg-white rounded-md p-4'>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Date</TableHead>
            <TableHead>Action</TableHead>
            <TableHead>Course Code</TableHead>
            <TableHead>Course Name</TableHead>
            <TableHead>Semester</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {historyResponse.data.map((history) => (
            <TableRow key={history.id}>
              <TableCell>{formatDate(history.createdAt)}</TableCell>
              <TableCell>{getActionBadge(history.actionType)}</TableCell>
              <TableCell className='font-medium'>
                {history.course.code}
              </TableCell>
              <TableCell>{history.course.subject?.name}</TableCell>
              <TableCell>
                {history.course.year}, Semester {history.course.semester}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <div className='py-4'>
        <TablePagination
          currentPage={page}
          totalPages={historyResponse.totalPages}
          totalItems={historyResponse.totalItems}
          pageSize={pageSize}
          onPageChange={setPage}
          onPageSizeChange={setPageSize}
        />
      </div>
    </div>
  );
};

export default EnrollmentHistoryTable;
