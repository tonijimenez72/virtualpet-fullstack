import { Gadget } from './gadget.enum';
import { Location } from './location.enum';

export interface Pet {
  id: string;
  userId: string;
  name: string;
  variety: string;
  color: string;
  happiness: number;
  energy: number;
  wisdom: number;
  gadget: Gadget;
  location: Location;
}
