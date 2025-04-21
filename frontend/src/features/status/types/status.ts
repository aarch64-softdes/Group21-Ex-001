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
  id: data.id.toString(),
  name: data.name,
  allowedTransitions: data.allowedTransitions?.map((t: any) => ({
    id: (t.id || t.toId).toString(),
    name: t.name,
  })),
});
