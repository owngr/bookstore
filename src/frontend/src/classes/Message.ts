export interface Message {
  severity: string;
  content: string;
}

export type MessageEvent = {
  notify: Message;
};
