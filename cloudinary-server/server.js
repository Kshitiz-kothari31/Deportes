require('dotenv').config();
const express = require('express');
const { createClient } = require('@supabase/supabase-js');

const app = express();
const PORT = process.env.PORT || 3000;

// Supabase config
const supabase = createClient(
  process.env.SUPABASE_URL,
  process.env.SUPABASE_SERVICE_ROLE_KEY
);

// GET /getVideos?sport=Football
app.get('/getVideos', async (req, res) => {
  const folder = req.query.sport; // e.g. "Football"

  if (!folder) {
    return res.status(400).json({ error: "Missing 'sport' parameter" });
  }

  try {
    // List files inside the folder in 'videos' bucket
    const { data: files, error } = await supabase
      .storage
      .from(process.env.SUPABASE_BUCKET)     // hardcoded bucket name (or use process.env.SUPABASE_BUCKET if set)
      .list(folder, { limit: 100 });

    if (error) {
      console.error("List error:", error.message);
      return res.status(500).json({ error: error.message });
    }

    if (!files || files.length === 0) {
      return res.status(404).json({ error: "No videos found in folder" });
    }

    // Map files to public URLs
    const videoUrls = files.map(file => {
      const { data } = supabase
        .storage
        .from(process.env.SUPABASE_BUCKET)
        .getPublicUrl(`${folder}/${file.name}`);

      console.log("Public URL:", data.publicUrl);
      return data.publicUrl;
    });

    return res.json({ videos: videoUrls });

  } catch (err) {
    console.error("Server error:", err);
    return res.status(500).json({ error: "Failed to fetch videos" });
  }
});


app.listen(PORT, '0.0.0.0', () => {
  console.log(`Server running on http://0.0.0.0:${PORT}`);
});
