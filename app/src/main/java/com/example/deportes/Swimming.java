package com.example.deportes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Swimming extends AppCompatActivity {

    private String[] videoUrls = {
            "", // breathing
            "", // floating
            "", // cordinating limbs
            "", // kicking
            "", // free_styles
            "", // breast stroke
            "", // back stroke
            "" // butterfly
    };

    private String[] videoTexts = {
            "Here are the short steps to learn breathing in swimming:\n" +
                    "\n" +
                    "1. Practice Exhaling in Water: Start by standing in shallow water. Take a deep breath, submerge your face, and exhale slowly through your nose or mouth. Come up for air and repeat.\n" +
                    "\n" +
                    "2. Focus on Rhythm: Match your breath with your strokes or kicks. For example, breathe in quickly through your mouth when you turn your head or lift it, and exhale slowly under the water.\n" +
                    "\n" +
                    "3. Use a Kickboard for Support: Hold onto a kickboard, kick gently, and practice turning your head to the side to breathe in while keeping your exhalation consistent underwater.\n" +
                    "\n" +
                    "4. Integrate Breathing with Strokes: Start with freestyle or breaststroke. Practice breathing every 2-3 strokes (freestyle) or during each stroke cycle (breaststroke).\n" +
                    "\n" +
                    "5. Relax Your Body: Stay relaxed to avoid panicking or holding your breath too long. This will help you maintain a steady rhythm.\n" +
                    "\n" +
                    "6. Practice in Sets: Alternate between swimming laps and resting to build confidence and endurance.\n" +
                    "\n" +
                    "7. Seek Feedback: Ask a coach or friend to watch your breathing technique and suggest improvements.\n\n",

            "Relax – Stay calm and breathe deeply.\n\n" +
                    "1. Position – Lie on your back or front.\n\n" +
                    "2. Spread Out – Extend arms and legs wide.\n\n" +
                    "3. Balance – Tilt hips up and arch slightly.\n\n" +
                    "4. Practice – Start in shallow water.\n\n",

            "Coordinating your limbs in swimming involves syncing your arms, legs, and breathing effectively. Here are the steps to improve limb coordination:\n\n" +
                    "1. Start with a Single Stroke\n\n" +
                    "   Focus on one stroke (e.g., freestyle or breaststroke). Mastering one helps build coordination gradually.\n" +
                    "\n" +
                    "2. Perfect Body Position\n\n" +
                    "   Keep your body horizontal in the water. A streamlined position reduces drag and helps limbs move more efficiently.\n" +
                    "\n" +
                    "3. Break Down Movements\n" +
                    "\n" +
                    "   Arms: Practice arm movements separately, ensuring a smooth rhythm.\n" +
                    "   Legs: Isolate kicking drills with a kickboard to focus on leg movement.\n" +
                    "4. Sync Arms and Legs\n\n" +
                    "   Combine arm and leg movements gradually. For example, in freestyle, time your kick to support arm strokes.\n" +
                    "\n" +
                    "5. Add Breathing\n\n" +
                    "   Integrate breathing into your stroke. Practice turning your head to breathe without disrupting limb movement.\n" +
                    "\n" +
                    "6. Use Drills\n\n" +
                    "   Practice drills like \"catch-up freestyle\" (one arm stays extended while the other completes a stroke) to fine-tune coordination.\n" +
                    "\n" +
                    "7. Practice Slowly\n\n" +
                    "   Swim slowly to focus on proper movement and timing. Gradually increase speed as coordination improves.\n" +
                    "\n" +
                    "8. Seek Feedback\n\n" +
                    "   Record yourself or get feedback from a coach to identify and correct timing issues.\n" +
                    "\n" +
                    "9. Build Muscle Memory\n\n" +
                    "   Repetition is key. Regular practice helps your body learn and automate movements.\n" +
                    "\n" +
                    "10. Be Patient\n\n" +
                    "    Progress takes time. Stay consistent and focus on small improvements each session.\n\n",

            "Pick a Kick Style: Flutter, dolphin, or frog.\n\n" +
                    "1. Use a Kickboard: Practice with support.\n\n" +
                    "2. Body Position: Stay flat, core tight.\n\n" +
                    "3. Move from Hips: Small, steady kicks.\n\n" +
                    "4. Relax Ankles: Point toes naturally.\n\n" +
                    "5. Practice Often: Build strength and rhythm.\n\n",

            "Here are short steps to learn freestyle swimming:\n" +
                    "\n" +
                    "1. Body Position: Keep your body flat and streamlined in the water.\n" +
                    "2. Kicking: Practice flutter kicks from the hips with pointed toes.\n" +
                    "3. Arm Movement: Alternate your arms in a windmill motion, pulling through the water.\n" +
                    "4. Breathing: Turn your head to the side to breathe, keeping one ear in the water.\n" +
                    "5. Coordination: Synchronize your arms, legs, and breathing.\n" +
                    "6. Practice Drills: Use a kickboard or practice one arm at a time to improve technique.\n" +
                    "7. Focus on Efficiency: Keep movements smooth and avoid splashing.\n\n",

            "Here are short steps to learn the breaststroke in swimming:\n" +
                    "\n" +
                    "1. Body Position:\n\n" +
                    "   Float flat on your stomach, keeping your body horizontal and streamlined.\n" +
                    "\n" +
                    "2. Arm Movement:\n" +
                    "\n" +
                    "   Start with your hands together, extended forward.\n" +
                    "   Pull your arms out and back in a circular motion, ending at your chest.\n" +
                    "   Recover by bringing your hands back together in front.\n\n" +
                    "3. Leg Kick:\n" +
                    "\n" +
                    "   Bend your knees, bringing your heels toward your butt.\n" +
                    "   Turn your feet outward and kick in a wide circular motion.\n" +
                    "   Finish by snapping your legs together straight.\n\n" +
                    "4. Breathing:\n" +
                    "\n" +
                    "   Lift your head slightly to inhale during the arm pull.\n" +
                    "   Submerge your face back into the water during the arm recovery.\n\n" +
                    "5. Timing:\n" +
                    "\n" +
                    "   Coordinate the pull, kick, and glide.\n" +
                    "   Use this rhythm: Pull, breathe, kick, glide.\n\n" +
                    "6. Practice Gliding:\n" +
                    "   After each kick, focus on gliding to improve efficiency.\n\n",

            "Here are the short steps to learn backstroke in swimming:\n" +
                    "\n" +
                    "1. Get Comfortable in Water: Start by getting used to floating on your back and breathing while lying flat in the water.\n" +
                    "\n" +
                    "2. Body Position: Keep your body straight, horizontal, and slightly elevated in the water. Your head should be relaxed, with your face above the water, and your body should remain as streamlined as possible.\n" +
                    "\n" +
                    "3. Arm Technique: Alternate your arms in a circular motion. As one arm moves down and back in the water, the other arm comes out of the water and begins to rotate forward. Your arms should move in an S-shape pattern under the water.\n" +
                    "\n" +
                    "4. Leg Movement: Perform a flutter kick, where your legs alternate kicking up and down from the hips. Keep your legs straight but relaxed, with a small bend at the knees.\n" +
                    "\n" +
                    "5. Breathing: Since your face is above the water, you can breathe freely. Make sure to maintain a steady and calm breathing rhythm.\n" +
                    "\n" +
                    "6. Coordination: Practice coordinating your arm strokes and flutter kicks in a smooth, continuous motion to maintain a steady speed.\n" +
                    "\n" +
                    "7. Practice: Start slowly and focus on your form, gradually increasing your speed as you become more comfortable.\n\n",

            "To learn the butterfly stroke in swimming, follow these short steps:\n" +
                    "\n" +
                    "1. Master the Body Position: Keep your body horizontal and streamlined in the water, with your head in a neutral position, facing downward.\n" +
                    "\n" +
                    "2. Arm Movement:\n" +
                    "\n" +
                    "   Move both arms simultaneously in a windmill motion.\n" +
                    "   Pull both arms underwater in a circular motion — the hands should sweep outward, downward, then inward toward the chest.\n" +
                    "   Recover the arms above water, keeping them close to the body.\n\n" +
                    "3. Leg Kick (Dolphin Kick):\n" +
                    "\n" +
                    "   Keep your legs together and use a fluid up-and-down motion with a strong kick from your hips.\n" +
                    "   The movement should come from your core, not your knees.\n\n" +
                    "4. Breathing Technique:\n" +
                    "\n" +
                    "   Lift your head slightly out of the water as your arms move forward during the recovery phase to inhale.\n" +
                    "   Exhale when your face is in the water, between arm strokes.\n\n" +
                    "5. Timing and Coordination:\n" +
                    "\n" +
                    "   The arms and legs should move in a synchronized rhythm: arms pull while the legs kick, then both arms recover as your legs rest momentarily before the next kick.\n\n" +
                    "6. Practice: Focus on small, consistent improvements in technique, balance, and rhythm before increasing speed.\n\n"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_swimming);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton s_breathing, s_floating, s_cordinatingLimbs, s_kicking, s_free_style, s_breast_stroke, s_back_stroke, s_butterfly;

        s_breathing = findViewById(R.id.swimming_breathing);
        s_breathing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(0);
            }
        });

        s_floating = findViewById(R.id.swimming_floating);
        s_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(1);
            }
        });

        s_cordinatingLimbs = findViewById(R.id.swimming_cordinatinlimbs);
        s_cordinatingLimbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(2);
            }
        });

        s_kicking = findViewById(R.id.swimming_kicking);
        s_kicking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(3);
            }
        });

        s_free_style = findViewById(R.id.swimming_free_style);
        s_free_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(4);
            }
        });

        s_breast_stroke = findViewById(R.id.swimming_breast_stroke);
        s_breast_stroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(5);
            }
        });

        s_back_stroke = findViewById(R.id.swimming_back_stroke);
        s_back_stroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(6);
            }
        });

        s_butterfly = findViewById(R.id.swimming_butterfly);
        s_butterfly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(7);
            }
        });
    }

    private void playVideo(int index)
    {
        if(index >= 0 && index < videoUrls.length)
        {
            Intent intent = new Intent(Swimming.this, Video.class);
            Uri videoUri = Uri.parse(videoUrls[index]);
            intent.putExtra("videoUri", videoUri.toString());
            intent.putExtra("videoText", videoTexts[index]);
            startActivity(intent);
        }
    }
}