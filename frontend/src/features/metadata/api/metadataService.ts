import api from '@/core/config/api';

export interface MetadataItem {
  name: string;
  value: string;
}

export default class MetadataService {
  getGenders = async (): Promise<string[]> => {
    const response = await api.get('/metadata/gender');
    return response.data;
  };

  getIdentityTypes = async (): Promise<string[]> => {
    const response = await api.get('/metadata/identity-type');
    return response.data;
  };
}
