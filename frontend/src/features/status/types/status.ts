export default interface Status {
  id: string;
  name: string;
  allowedTransitions?: Status[];
}

export interface CreateStatusDTO {
  name: string;
  allowedTransitions?: Status[];
}

export interface UpdateStatusDTO {
  name: string;
  allowedTransitions?: Status[];
}

export const mapToStatus = (data: any): Status => ({
  id: data.id,
  name: data.name,
  allowedTransitions: data.allowedTransitions,
});
