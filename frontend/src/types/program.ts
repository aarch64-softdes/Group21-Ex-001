export default interface Program {
    id: number;
    name: string;
}

export interface CreateProgramDTO {
    name: string;
}

export interface UpdateProgramDTO {
    name: string;
}

export const mapToProgram = (data: any): Program => ({
    id: data.id,
    name: data.name,
});
