import {UserDTO} from "./UserDTO";
import {Status} from "../Status";

export interface SearchReq{
  subject:string | null;
  assignedTo:UserDTO | null;
  dueDate:Date | null;
  status:Status | null;
}
