public class FileType {
    public String getType(byte[] file){
        if (file.length > 16){
            if(file[0] == (byte) 0xFF && file[1] == (byte) 0xD8 && file[2] == (byte) 0xFF && file[3] == (byte) 0xE0)    return "jpg";
            if(file[0] == (byte) 0xFF && file[1] == (byte) 0xD8 && file[2] == (byte) 0xFF && file[3] == (byte) 0xEE)    return "jpeg";
            if(file[0] == (byte) 0x89 && file[1] == (byte) 0x50 && file[2] == (byte) 0x4E && file[3] == (byte) 0x47 && file[4] == (byte) 0x0D && file[5] == (byte) 0x0A && file[6] == (byte) 0x1A && file[7] == (byte) 0x0A)    return "png";
            if(file[4] == (byte) 0x66 && file[5] == (byte) 0x74 && file[6] == (byte) 0x79 && file[7] == (byte) 0x70 && file[8] == (byte) 0x69 && file[9] == (byte) 0x73 && file[10] == (byte) 0x6F && file[11] == (byte) 0x6D)    return "mp4";
            else return "";
        }
        return "";
    }
}
