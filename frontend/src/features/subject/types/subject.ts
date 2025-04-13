export default interface Subject {
  id: string;
  name: string;
  code: string;
  credits: number;
  description?: string;
  faculty?: string;
  prerequisites?: Partial<Subject>[];
}

export interface CreateSubjectDTO {
  name: string;
  code: string;
  credits: number;
  description?: string;
  facultyId?: string;
  prerequisites?: string[];
}

export interface UpdateSubjectDTO {
  name?: string;
  code?: string;
  credits?: number;
  description?: string;
  facultyId?: string;
  prerequisites?: string[];
}

export const mapToSubject = (data: any): Subject => ({
  id: data.id.toString(),
  name: data.name,
  code: data.code,
  credits: data.credits,
  description: data.description,
  faculty: data.faculty,
  prerequisites: data.prerequisitesSubjects?.map((subject: any) => ({
    id: subject.id.toString(),
    name: subject.name,
    code: subject.code,
    credits: subject.credits,
    description: subject.description,
    faculty: subject.faculty,
  })),
});
