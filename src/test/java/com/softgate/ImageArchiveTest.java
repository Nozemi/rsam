package com.softgate;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import com.softgate.fs.FileStore;
import com.softgate.fs.IndexedFileSystem;
import com.softgate.fs.binary.Archive;
import com.softgate.fs.binary.Archive.ArchiveEntry;
import com.softgate.fs.binary.ImageArchive;
import com.softgate.fs.binary.Sprite;
import com.softgate.util.HashUtils;

public class ImageArchiveTest {

	public static void main(String[] args) throws IOException {
		IndexedFileSystem fs = IndexedFileSystem.init(Paths.get("./cache/"));
		
		// store containing archives
		FileStore store = fs.getStore(0);
		
		// 2d graphics archive
		Archive archive = Archive.decode(store.readFile(4));
		
		for (int file = 0; file < archive.getEntries().size(); file++) {
			
			ArchiveEntry entry = archive.getEntries().get(file);
			
			ArchiveEntry idxEntry = archive.getEntry("index.dat");
			
			if (entry.getHash() == HashUtils.nameToHash("index.dat")) {
				continue;
			}
			
			byte[] data = archive.readFile(entry.getHash());
			
			byte[] idx = archive.readFile(idxEntry.getHash());
			
			List<Sprite> sprites = ImageArchive.decode(ByteBuffer.wrap(data), ByteBuffer.wrap(idx), true);
			
			System.out.println(sprites.size());
			
			for (int spriteId = 0; spriteId < sprites.size(); spriteId++) {			
				Sprite sprite = sprites.get(spriteId);
				
				ImageIO.write(sprite.toBufferedImage(), "png", new File("./dump/" + file + "_" + spriteId + ".png"));
				
				System.out.println("file= " + file + " sprite= " + spriteId);
			}
		}
		

		
	}

}
