import api from '@/core/config/api';

export interface MetadataItem {
  name: string;
  value: string;
}

export default class MetadataService {
  getFaculties = async (): Promise<MetadataItem[]> => {
    const response = await api.get('/metadata/faculty');
    return response.data;
  };

  getGenders = async (): Promise<String[]> => {
    const response = await api.get('/metadata/gender');
    return response.data;
  };

  getStudentStatuses = async (): Promise<String[]> => {
    const response = await api.get('/metadata/student-status');
    return response.data;
  };
}
