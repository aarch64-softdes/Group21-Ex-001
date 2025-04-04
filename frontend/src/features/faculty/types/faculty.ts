export default interface Faculty {
    id: string;
    name: string;
}

export interface CreateFacultyDTO {
    name: string;
}

export interface UpdateFacultyDTO {
    name: string;
}

export const mapToFaculty = (data: any): Faculty => ({
    id: data.id,
    name: data.name,
});
