// models/formation.model.ts
export class Formation {
  id?: number;
  title: string = '';
  description: string = '';
  startDate: Date = new Date();
  endDate: Date = new Date();
  price: number = 0;
  imageUrl?: string = '';
}


  