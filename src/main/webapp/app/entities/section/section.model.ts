export interface ISection {
  id: string;
  title?: string | null;
  description?: string | null;
  courseId?: string | null;
  text?: string | null;
  videoUrl?: string | null;
  videoId?: string | null;
  videoDuration?: string | null;
  videoTitle?: string | null;
  videoDescription?: string | null;
  videoChannelLanguage?: string | null;
}

export type NewSection = Omit<ISection, 'id'> & { id: null };
