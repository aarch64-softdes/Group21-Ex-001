export default interface Status {
    id: string;
    name: string;
}

export interface CreateStatusDTO {
    name: string;
}

export interface UpdateStatusDTO {
    name: string;
}

export const mapToStatus = (data: any): Status => ({
    id: data.id,
    name: data.name,
});
