import MetadataService from '@metadata/api/metadataService';
import { useQuery } from '@tanstack/react-query';

const metadataService = new MetadataService();

export const useGenders = () => {
  return useQuery<String[], Error>({
    queryKey: ['genders'],
    queryFn: () => metadataService.getGenders(),
    staleTime: 1000 * 60 * 60, // 1 hour
  });
};

export const useIdentityTypes = () => {
  return useQuery<String[], Error>({
    queryKey: ['identity-types'],
    queryFn: () => metadataService.getIdentityTypes(),
    staleTime: 1000 * 60 * 60, // 1 hour
  });
};
