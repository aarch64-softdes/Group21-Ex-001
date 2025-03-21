import axios from "axios";

export interface MetadataItem {
    name: string;
    value: string;
}

const api = axios.create({
    baseURL: "http://localhost:8080",
});

export default class MetadataService {
    getFaculties = async (): Promise<MetadataItem[]> => {
        const response = await api.get("/metadata/faculty");
        return response.data;
    };

    getGenders = async (): Promise<String[]> => {
        const response = await api.get("/metadata/gender");
        return response.data;
    };

    getStudentStatuses = async (): Promise<String[]> => {
        const response = await api.get("/metadata/student-status");
        return response.data;
    };
}
