import { useQuery } from "@tanstack/react-query";
import MetadataService, { MetadataItem } from "@/services/metadataService";

const metadataService = new MetadataService();

export const useFaculties = () => {
  return useQuery<MetadataItem[], Error>({
    queryKey: ["faculties"],
    queryFn: () => metadataService.getFaculties(),
    staleTime: 1000 * 60 * 60, // 1 hour
  });
};

export const useGenders = () => {
  return useQuery<String[], Error>({
    queryKey: ["genders"],
    queryFn: () => metadataService.getGenders(),
    staleTime: 1000 * 60 * 60, // 1 hour
  });
};

export const useStudentStatuses = () => {
  return useQuery<String[], Error>({
    queryKey: ["studentStatuses"],
    queryFn: () => metadataService.getStudentStatuses(),
    staleTime: 1000 * 60 * 60, // 1 hour
  });
};
