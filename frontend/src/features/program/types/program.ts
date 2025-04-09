export default interface Program {
  id: string;
  name: string;
}

export interface CreateProgramDTO {
  name: string;
}

export interface UpdateProgramDTO {
  name: string;
}

export const mapToProgram = (data: any): Program => ({
  id: data.id as string,
  name: data.name,
});
