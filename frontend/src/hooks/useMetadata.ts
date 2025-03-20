import FacultyService from "@/services/facultyService";
import MetadataService from "@/services/metadataService";
import ProgramService from "@/services/programService";
import StatusService from "@/services/statusService";
import { useQuery } from "@tanstack/react-query";

const metadataService = new MetadataService();
const facultyService = new FacultyService();
const programService = new ProgramService();
const statusService = new StatusService();

export const useGenders = () => {
    return useQuery<String[], Error>({
        queryKey: ["genders"],
        queryFn: () => metadataService.getGenders(),
        staleTime: 1000 * 60 * 60, // 1 hour
    });
};

export const useEntityFaculties = () => {
    return useQuery<String[], Error>({
        queryKey: ["entity-faculties"],
        queryFn: async () => {
            const response = await facultyService.getFaculties({});
            return response.data.map((faculty) => faculty.name);
        },
        staleTime: 1000 * 60 * 5,
    });
};

export const useEntityPrograms = () => {
    return useQuery<String[], Error>({
        queryKey: ["entity-programs"],
        queryFn: async () => {
            const response = await programService.getPrograms({});
            return response.data.map((program) => program.name);
        },
        staleTime: 1000 * 60 * 5,
    });
};

export const useEntityStatuses = () => {
    return useQuery<String[], Error>({
        queryKey: ["entity-statuses"],
        queryFn: async () => {
            const response = await statusService.getStatuses({});
            return response.data.map((status) => status.name);
        },
        staleTime: 1000 * 60 * 5,
    });
};
