-- =====================================================================
-- Art Supplies E-Commerce :: Core PostgreSQL Schema
-- Local bootstrap DDL + seed (executed by Spring SQL init, mode=always).
-- Shape mirrors the frontend `@/types/api` Category & Product contracts:
--   Category { id, slug, name, tagline }
--   Product  { id, categoryId, slug, name, description, price, stock, imageUrl }
-- String ids (e.g. 'cat-paint', 'p-001') match the frontend seed catalog.
-- Dropped and recreated each boot so schema changes always take effect.
-- =====================================================================

DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;

-- ---------------------------------------------------------------------
-- Table: categories
-- ---------------------------------------------------------------------
CREATE TABLE categories (
    id      VARCHAR(40)  PRIMARY KEY,
    slug    VARCHAR(80)  NOT NULL UNIQUE,
    name    VARCHAR(120) NOT NULL UNIQUE,
    tagline VARCHAR(255)
);

-- ---------------------------------------------------------------------
-- Table: products
--   FK: products.category_id -> categories.id
-- ---------------------------------------------------------------------
CREATE TABLE products (
    id          VARCHAR(40)    PRIMARY KEY,
    category_id VARCHAR(40)    NOT NULL,
    slug        VARCHAR(120)   NOT NULL UNIQUE,
    name        VARCHAR(200)   NOT NULL,
    description TEXT,
    price       NUMERIC(12, 2) NOT NULL CHECK (price >= 0),
    stock       INTEGER        NOT NULL DEFAULT 0 CHECK (stock >= 0),
    image_url   TEXT,
    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id) REFERENCES categories (id)
        ON DELETE RESTRICT
);

-- Index supporting the hot path: list/filter products by category.
CREATE INDEX idx_products_category_id ON products (category_id);

-- =====================================================================
-- Seed data :: 6 categories, 15 products each
-- =====================================================================

INSERT INTO categories (id, slug, name, tagline) VALUES
    ('cat-paint',   'paint',    'Paint',             'Pigments with depth, body, and permanence.'),
    ('cat-brushes', 'brushes',  'Brushes',           'Hand-finished bristles for every gesture.'),
    ('cat-paper',   'paper',    'Paper & Surfaces',  'Cold-pressed grounds that hold their tone.'),
    ('cat-drawing', 'drawing',  'Drawing',           'Graphite, ink, and charcoal of rare clarity.'),
    ('cat-pastels', 'pastels',  'Pastels',           'Velvet sticks of pure, crumbling colour.'),
    ('cat-easels',  'easels',   'Easels & Studio',   'Steady frames and quiet studio essentials.');

INSERT INTO products (id, category_id, slug, name, description, price, stock, image_url) VALUES
    -- ----- Paint -----
    ('p-001','cat-paint','cobalt-blue-oil-paint','Cobalt Blue Oil Paint','Single-pigment cobalt milled in cold-pressed linseed - buttery, slow-drying, and luminous for skies and quiet distance.',38.00,12,'https://images.unsplash.com/photo-1513364776144-60967b0f800f?auto=format&fit=crop&w=1200&q=80'),
    ('p-002','cat-paint','titanium-white-oil-paint','Titanium White Oil Paint','A dense, opaque white with clean tinting strength and a soft satin finish that holds every knife mark.',21.00,44,'https://images.unsplash.com/photo-1502691876148-a84978e59af8?auto=format&fit=crop&w=1200&q=80'),
    ('p-003','cat-paint','cadmium-red-medium-oil','Cadmium Red Medium Oil','Genuine cadmium red of fierce opacity and warmth - the immovable anchor of a portrait palette.',34.00,18,'https://images.unsplash.com/photo-1493106641515-6b5631de4bb9?auto=format&fit=crop&w=1200&q=80'),
    ('p-004','cat-paint','yellow-ochre-oil-paint','Yellow Ochre Oil Paint','An earthy, semi-transparent ochre ground from natural iron oxide, indispensable for flesh and landscape.',19.00,30,'https://images.unsplash.com/photo-1579965342575-16428a7c8881?auto=format&fit=crop&w=1200&q=80'),
    ('p-005','cat-paint','ultramarine-blue-oil','Ultramarine Blue Oil','A deep, transparent ultramarine that glazes to violet shadow and mixes a thousand quiet greys.',22.00,26,'https://images.unsplash.com/photo-1460661419201-fd4cecdf8a8b?auto=format&fit=crop&w=1200&q=80'),
    ('p-006','cat-paint','titanium-white-acrylic','Titanium White Acrylic','Heavy-body acrylic with maximum opacity and a velvet matte film that resists yellowing for a lifetime.',22.00,40,'https://images.unsplash.com/photo-1513364776144-60967b0f800f?auto=format&fit=crop&w=1200&q=80'),
    ('p-007','cat-paint','phthalo-green-acrylic','Phthalo Green Acrylic','A staining, near-electric green of immense tinting power - a single drop transforms a mixing pool.',18.00,24,'https://images.unsplash.com/photo-1502691876148-a84978e59af8?auto=format&fit=crop&w=1200&q=80'),
    ('p-008','cat-paint','quinacridone-magenta-acrylic','Quinacridone Magenta Acrylic','Transparent, lightfast magenta that glows in glazes and snaps cool mixes into life.',20.00,28,'https://images.unsplash.com/photo-1493106641515-6b5631de4bb9?auto=format&fit=crop&w=1200&q=80'),
    ('p-009','cat-paint','designer-gouache-primary-set','Designer Gouache, Primary Set','Six tubes of opaque, re-wettable gouache with a chalk-matte flatness - the illustrator''s quiet workhorse.',42.00,14,'https://images.unsplash.com/photo-1579965342575-16428a7c8881?auto=format&fit=crop&w=1200&q=80'),
    ('p-010','cat-paint','mars-black-gouache','Mars Black Gouache','A dense, matte, perfectly flat black that re-wets cleanly and never dries shiny.',11.00,33,'https://images.unsplash.com/photo-1460661419201-fd4cecdf8a8b?auto=format&fit=crop&w=1200&q=80'),
    ('p-011','cat-paint','watercolour-half-pan-set-24','Watercolour Half-Pan Set, 24','Twenty-four artist half-pans of finely milled, highly pigmented colour in an enamelled travel tin.',96.00,10,'https://images.unsplash.com/photo-1513364776144-60967b0f800f?auto=format&fit=crop&w=1200&q=80'),
    ('p-012','cat-paint','burnt-sienna-watercolour','Burnt Sienna Watercolour','A warm, granulating earth that floods into glowing washes and lifts cleanly off cotton paper.',13.00,36,'https://images.unsplash.com/photo-1502691876148-a84978e59af8?auto=format&fit=crop&w=1200&q=80'),
    ('p-013','cat-paint','paynes-grey-watercolour','Payne''s Grey Watercolour','A versatile blue-black neutral that deepens shadow without ever deadening it.',12.00,38,'https://images.unsplash.com/photo-1493106641515-6b5631de4bb9?auto=format&fit=crop&w=1200&q=80'),
    ('p-014','cat-paint','iridescent-gold-acrylic-ink','Iridescent Gold Acrylic Ink','A shimmering, pigment-rich gold ink that pools to a metallic sheen for lettering and accents.',17.00,20,'https://images.unsplash.com/photo-1579965342575-16428a7c8881?auto=format&fit=crop&w=1200&q=80'),
    ('p-015','cat-paint','cadmium-yellow-lemon-oil','Cadmium Yellow Lemon Oil','A cool, brilliant lemon cadmium of high opacity - the cleanest path to luminous greens.',36.00,16,'https://images.unsplash.com/photo-1460661419201-fd4cecdf8a8b?auto=format&fit=crop&w=1200&q=80'),
    -- ----- Brushes -----
    ('p-016','cat-brushes','kolinsky-sable-round-no-6','Kolinsky Sable Round, No. 6','The benchmark watercolour round - flawless point, generous belly, and instant snap-back, hairline to flooded wash.',64.00,8,'https://images.unsplash.com/photo-1456735190827-d1262f71b8a3?auto=format&fit=crop&w=1200&q=80'),
    ('p-017','cat-brushes','kolinsky-sable-round-no-2','Kolinsky Sable Round, No. 2','A fine detail round that keeps a needle tip yet carries a surprising load of colour.',38.00,14,'https://images.unsplash.com/photo-1572044162444-ad60f128bdea?auto=format&fit=crop&w=1200&q=80'),
    ('p-018','cat-brushes','kolinsky-sable-round-no-10','Kolinsky Sable Round, No. 10','A large pointed round for confident, single-stroke washes and bold calligraphic marks.',92.00,6,'https://images.unsplash.com/photo-1456735190827-d1262f71b8a3?auto=format&fit=crop&w=1200&q=80'),
    ('p-019','cat-brushes','hog-bristle-filbert-set','Hog Bristle Filbert Set','Five interlocked hog filberts for oil and heavy acrylic - the natural flag feathers blends without hard edges.',49.00,16,'https://images.unsplash.com/photo-1572044162444-ad60f128bdea?auto=format&fit=crop&w=1200&q=80'),
    ('p-020','cat-brushes','hog-bristle-flat-1-inch','Hog Bristle Flat, 1 inch','A springy, full-bodied flat that lays broad, even passages and carves crisp edges on its corner.',16.00,28,'https://images.unsplash.com/photo-1456735190827-d1262f71b8a3?auto=format&fit=crop&w=1200&q=80'),
    ('p-021','cat-brushes','synthetic-fan-blender','Synthetic Fan Blender','A soft synthetic fan for feathering skies, foliage, and hair into silk-smooth transitions.',21.00,22,'https://images.unsplash.com/photo-1572044162444-ad60f128bdea?auto=format&fit=crop&w=1200&q=80'),
    ('p-022','cat-brushes','sable-rigger-no-1','Sable Rigger, No. 1','An extra-long sable rigger that draws unbroken lines - rigging, branches, whiskers, and signatures.',27.00,18,'https://images.unsplash.com/photo-1456735190827-d1262f71b8a3?auto=format&fit=crop&w=1200&q=80'),
    ('p-023','cat-brushes','squirrel-mop-large','Squirrel Mop, Large','A plump squirrel mop that holds a reservoir of wash and releases it in one slow, even flood.',44.00,12,'https://images.unsplash.com/photo-1572044162444-ad60f128bdea?auto=format&fit=crop&w=1200&q=80'),
    ('p-024','cat-brushes','synthetic-flat-wash-2-inch','Synthetic Flat Wash, 2 inch','A wide, thirsty synthetic flat for skies and gradients laid across full sheets.',23.00,20,'https://images.unsplash.com/photo-1456735190827-d1262f71b8a3?auto=format&fit=crop&w=1200&q=80'),
    ('p-025','cat-brushes','angular-shader-half-inch','Angular Shader, 1/2 inch','A slanted synthetic angle for tight corners, petals, and controlled chisel strokes.',14.00,30,'https://images.unsplash.com/photo-1572044162444-ad60f128bdea?auto=format&fit=crop&w=1200&q=80'),
    ('p-026','cat-brushes','detail-liner-trio','Detail Liner Trio','Three fine synthetic liners - 10/0, 5/0, and 0 - for the smallest marks a steady hand can make.',19.00,26,'https://images.unsplash.com/photo-1456735190827-d1262f71b8a3?auto=format&fit=crop&w=1200&q=80'),
    ('p-027','cat-brushes','travel-brush-pocket-round','Travel Brush, Pocket Round','A sable pocket round that caps into its own handle - full-size performance that folds into a coat pocket.',33.00,15,'https://images.unsplash.com/photo-1572044162444-ad60f128bdea?auto=format&fit=crop&w=1200&q=80'),
    ('p-028','cat-brushes','palette-painting-knife-set','Palette & Painting Knife Set','Five forged, mirror-polished steel knives, flex-tempered for buttery mixing and crisp impasto.',36.00,17,'https://images.unsplash.com/photo-1456735190827-d1262f71b8a3?auto=format&fit=crop&w=1200&q=80'),
    ('p-029','cat-brushes','mottler-varnish-brush-2-inch','Mottler Varnish Brush, 2 inch','A soft, lint-free mottler that lays an even varnish coat with no brush marks or shed hairs.',29.00,13,'https://images.unsplash.com/photo-1572044162444-ad60f128bdea?auto=format&fit=crop&w=1200&q=80'),
    ('p-030','cat-brushes','bamboo-brush-large','Bamboo Brush, Large','A traditional goat-and-weasel bamboo brush for sumi-e, expressive line, and broad calligraphic wash.',18.00,24,'https://images.unsplash.com/photo-1456735190827-d1262f71b8a3?auto=format&fit=crop&w=1200&q=80'),
    -- ----- Paper & Surfaces -----
    ('p-031','cat-paper','cold-press-watercolour-block','Cold-Press Watercolour Block','Twenty sheets of 300gsm, 100% cotton paper gummed on four sides - stays dead flat through the heaviest wash.',34.00,25,'https://images.unsplash.com/photo-1517842645767-c639042777db?auto=format&fit=crop&w=1200&q=80'),
    ('p-032','cat-paper','hot-press-watercolour-block','Hot-Press Watercolour Block','A smooth, near-plate cotton surface for fluid detail, botanical work, and crisp ink line.',36.00,20,'https://images.unsplash.com/photo-1531346878377-a5be20888e57?auto=format&fit=crop&w=1200&q=80'),
    ('p-033','cat-paper','rough-watercolour-sheets-10','Rough Watercolour Sheets, 10','Ten loose sheets of heavyweight rough cotton - pronounced tooth that granulates and sparkles.',39.00,18,'https://images.unsplash.com/photo-1597484661643-2f5fef640dd1?auto=format&fit=crop&w=1200&q=80'),
    ('p-034','cat-paper','toned-grey-sketchbook','Toned Grey Sketchbook','A mid-grey ground that carries the midtones while you push light and shadow - 120gsm, lay-flat linen hardcover.',19.00,50,'https://images.unsplash.com/photo-1517842645767-c639042777db?auto=format&fit=crop&w=1200&q=80'),
    ('p-035','cat-paper','toned-tan-sketchbook','Toned Tan Sketchbook','A warm tan ground built for charcoal, chalk, and coloured-pencil studies.',19.00,46,'https://images.unsplash.com/photo-1531346878377-a5be20888e57?auto=format&fit=crop&w=1200&q=80'),
    ('p-036','cat-paper','bristol-vellum-pad','Bristol Vellum Pad','Heavyweight, brilliant-white two-ply Bristol with a faint tooth - takes ink, marker, and repeated erasing.',24.00,18,'https://images.unsplash.com/photo-1597484661643-2f5fef640dd1?auto=format&fit=crop&w=1200&q=80'),
    ('p-037','cat-paper','bristol-smooth-pad','Bristol Smooth Pad','A glass-smooth plate Bristol for pen, fineliner, and airbrush with zero feathering.',24.00,21,'https://images.unsplash.com/photo-1517842645767-c639042777db?auto=format&fit=crop&w=1200&q=80'),
    ('p-038','cat-paper','mixed-media-spiral-pad','Mixed-Media Spiral Pad','A 250gsm wire-bound pad that takes wet and dry media alike without buckling.',17.00,34,'https://images.unsplash.com/photo-1531346878377-a5be20888e57?auto=format&fit=crop&w=1200&q=80'),
    ('p-039','cat-paper','kraft-toned-paper-pad','Kraft Toned Paper Pad','Warm kraft sheets for gouache, ink, and gel-pen work with built-in atmosphere.',13.00,40,'https://images.unsplash.com/photo-1597484661643-2f5fef640dd1?auto=format&fit=crop&w=1200&q=80'),
    ('p-040','cat-paper','linen-canvas-panel-12x16','Linen Canvas Panel, 12x16','Acid-free Belgian linen mounted to a rigid board - oil-ready, archival, and travel-tough.',28.00,22,'https://images.unsplash.com/photo-1517842645767-c639042777db?auto=format&fit=crop&w=1200&q=80'),
    ('p-041','cat-paper','stretched-cotton-canvas-18x24','Stretched Cotton Canvas, 18x24','Triple-primed cotton on a kiln-dried bar, ready for oil or acrylic straight off the shelf.',32.00,19,'https://images.unsplash.com/photo-1531346878377-a5be20888e57?auto=format&fit=crop&w=1200&q=80'),
    ('p-042','cat-paper','watercolour-postcard-pack','Watercolour Postcard Pack','Twenty cotton postcards, gummed and ready to paint, stamp, and send.',15.00,44,'https://images.unsplash.com/photo-1597484661643-2f5fef640dd1?auto=format&fit=crop&w=1200&q=80'),
    ('p-043','cat-paper','printmaking-paper-sheets-10','Printmaking Paper Sheets, 10','Ten sheets of soft, sized mould-made paper that takes etching and relief ink evenly.',41.00,14,'https://images.unsplash.com/photo-1517842645767-c639042777db?auto=format&fit=crop&w=1200&q=80'),
    ('p-044','cat-paper','pastel-paper-pad-assorted','Pastel Paper Pad, Assorted','Twenty-four sheets of toothy, coloured pastel paper that grips pigment layer after layer.',22.00,27,'https://images.unsplash.com/photo-1531346878377-a5be20888e57?auto=format&fit=crop&w=1200&q=80'),
    ('p-045','cat-paper','hardbound-watercolour-journal','Hardbound Watercolour Journal','A stitched 200gsm cotton journal that lies flat and survives a soaking on location.',29.00,23,'https://images.unsplash.com/photo-1597484661643-2f5fef640dd1?auto=format&fit=crop&w=1200&q=80'),
    -- ----- Drawing -----
    ('p-046','cat-drawing','graphite-pencil-set-12-grades','Graphite Pencil Set, 12 Grades','From whisper-soft 6B to crisp 4H - twelve cedar-cased pencils with smooth, break-resistant cores.',28.00,30,'https://images.unsplash.com/photo-1488998427799-e3362cec87c3?auto=format&fit=crop&w=1200&q=80'),
    ('p-047','cat-drawing','carbon-india-ink','Carbon India Ink','A dense, lightfast carbon-black ink that flows without feathering and dries to a waterproof matte - 30ml.',16.00,0,'https://images.unsplash.com/photo-1611532736597-de2d4265fba3?auto=format&fit=crop&w=1200&q=80'),
    ('p-048','cat-drawing','soft-vine-charcoal-bundle','Soft Vine Charcoal Bundle','Naturally carbonized willow that glides on with a velvet bite and lifts away in a single pass - a dozen sticks.',12.00,35,'https://images.unsplash.com/photo-1602738328654-51ab2ae6c4ff?auto=format&fit=crop&w=1200&q=80'),
    ('p-049','cat-drawing','compressed-charcoal-set','Compressed Charcoal Set','Six rich, dark compressed sticks for deep blacks and bold, committed marks.',14.00,28,'https://images.unsplash.com/photo-1602738328654-51ab2ae6c4ff?auto=format&fit=crop&w=1200&q=80'),
    ('p-050','cat-drawing','charcoal-pencil-trio','Charcoal Pencil Trio','Soft, medium, and hard charcoal in a clean wood casing - control where vine charcoal won''t reach.',11.00,32,'https://images.unsplash.com/photo-1488998427799-e3362cec87c3?auto=format&fit=crop&w=1200&q=80'),
    ('p-051','cat-drawing','woodless-graphite-set','Woodless Graphite Set','Solid graphite cores in four grades for broad tonal blocking and sharpened detail alike.',17.00,24,'https://images.unsplash.com/photo-1611532736597-de2d4265fba3?auto=format&fit=crop&w=1200&q=80'),
    ('p-052','cat-drawing','mechanical-pencil-05mm','Mechanical Pencil, 0.5mm','A brass-weighted draughting pencil with a cushioned lead and a knurled, precise grip.',26.00,20,'https://images.unsplash.com/photo-1488998427799-e3362cec87c3?auto=format&fit=crop&w=1200&q=80'),
    ('p-053','cat-drawing','kneaded-eraser-pack','Kneaded Eraser Pack','Three soft, mouldable erasers that lift highlights and tone without a single crumb.',6.00,60,'https://images.unsplash.com/photo-1602738328654-51ab2ae6c4ff?auto=format&fit=crop&w=1200&q=80'),
    ('p-054','cat-drawing','fineliner-drawing-set','Fineliner Drawing Set','Eight pigment fineliners, archival and waterproof, from 0.05 to a brush nib.',21.00,30,'https://images.unsplash.com/photo-1611532736597-de2d4265fba3?auto=format&fit=crop&w=1200&q=80'),
    ('p-055','cat-drawing','dip-pen-nib-set','Dip Pen & Nib Set','A lacquered holder with five assorted nibs - flex, crow-quill, and italic for line and lettering.',23.00,18,'https://images.unsplash.com/photo-1611532736597-de2d4265fba3?auto=format&fit=crop&w=1200&q=80'),
    ('p-056','cat-drawing','sepia-drawing-ink','Sepia Drawing Ink','A warm, transparent sepia that washes to soft brown and deepens with each layer - 30ml.',15.00,22,'https://images.unsplash.com/photo-1611532736597-de2d4265fba3?auto=format&fit=crop&w=1200&q=80'),
    ('p-057','cat-drawing','white-charcoal-pencils-pair','White Charcoal Pencils, Pair','Two chalk-white pencils for highlights and reverse drawing on toned grounds.',9.00,38,'https://images.unsplash.com/photo-1488998427799-e3362cec87c3?auto=format&fit=crop&w=1200&q=80'),
    ('p-058','cat-drawing','coloured-pencil-set-24','Coloured Pencil Set, 24','Twenty-four wax-based pencils with soft, layerable cores and brilliant, lightfast colour.',48.00,19,'https://images.unsplash.com/photo-1602738328654-51ab2ae6c4ff?auto=format&fit=crop&w=1200&q=80'),
    ('p-059','cat-drawing','blending-stump-set','Blending Stump Set','Six tightly rolled paper stumps and a tortillon for seamless graphite and charcoal blends.',8.00,44,'https://images.unsplash.com/photo-1488998427799-e3362cec87c3?auto=format&fit=crop&w=1200&q=80'),
    ('p-060','cat-drawing','metalpoint-silver-stylus','Metalpoint Silver Stylus','A genuine silver point for fine, slow lines that tarnish to a warm antique brown over time.',34.00,11,'https://images.unsplash.com/photo-1611532736597-de2d4265fba3?auto=format&fit=crop&w=1200&q=80'),
    -- ----- Pastels -----
    ('p-061','cat-pastels','soft-pastel-set-24-half-sticks','Soft Pastel Set, 24 Half-Sticks','Twenty-four buttery half-sticks of pure pigment that lay down velvet colour and blend with a fingertip.',58.00,16,'https://images.unsplash.com/photo-1493106641515-6b5631de4bb9?auto=format&fit=crop&w=1200&q=80'),
    ('p-062','cat-pastels','soft-pastel-set-48-full-sticks','Soft Pastel Set, 48 Full Sticks','A studio range of forty-eight full sticks - landscape and portrait tones in one cushioned tray.',112.00,9,'https://images.unsplash.com/photo-1579965342575-16428a7c8881?auto=format&fit=crop&w=1200&q=80'),
    ('p-063','cat-pastels','portrait-pastel-set-12','Portrait Pastel Set, 12','Twelve curated flesh and shadow tones for confident, low-fuss portrait work.',39.00,18,'https://images.unsplash.com/photo-1504333638930-c8787321eee0?auto=format&fit=crop&w=1200&q=80'),
    ('p-064','cat-pastels','landscape-pastel-set-12','Landscape Pastel Set, 12','Twelve sky, foliage, and earth greens chosen for plein-air speed.',39.00,17,'https://images.unsplash.com/photo-1607734834519-d8576ae60ea6?auto=format&fit=crop&w=1200&q=80'),
    ('p-065','cat-pastels','hard-pastel-set-24','Hard Pastel Set, 24','Square hard pastels that hold a sharp edge for underdrawing, detail, and crisp accents.',32.00,20,'https://images.unsplash.com/photo-1493106641515-6b5631de4bb9?auto=format&fit=crop&w=1200&q=80'),
    ('p-066','cat-pastels','pastel-pencil-set-12','Pastel Pencil Set, 12','Wood-cased pastel in twelve colours for the fine detail soft sticks can''t reach.',36.00,22,'https://images.unsplash.com/photo-1579965342575-16428a7c8881?auto=format&fit=crop&w=1200&q=80'),
    ('p-067','cat-pastels','oil-pastel-set-25','Oil Pastel Set, 25','Twenty-five rich, creamy oil pastels that smear, scrape, and layer like buttery paint.',44.00,15,'https://images.unsplash.com/photo-1504333638930-c8787321eee0?auto=format&fit=crop&w=1200&q=80'),
    ('p-068','cat-pastels','oil-pastel-set-iridescent-12','Oil Pastel Set, Iridescent 12','Twelve pearlescent oil pastels that catch the light with a metallic shimmer.',29.00,19,'https://images.unsplash.com/photo-1607734834519-d8576ae60ea6?auto=format&fit=crop&w=1200&q=80'),
    ('p-069','cat-pastels','grey-tone-pastel-set-12','Grey Tone Pastel Set, 12','A graded run of warm and cool greys for value studies and quiet atmosphere.',31.00,21,'https://images.unsplash.com/photo-1493106641515-6b5631de4bb9?auto=format&fit=crop&w=1200&q=80'),
    ('p-070','cat-pastels','single-soft-pastel-cobalt','Single Soft Pastel, Cobalt','An open-stock cobalt soft pastel for replacing the colour you always run out of.',4.00,80,'https://images.unsplash.com/photo-1579965342575-16428a7c8881?auto=format&fit=crop&w=1200&q=80'),
    ('p-071','cat-pastels','single-soft-pastel-permanent-red','Single Soft Pastel, Permanent Red','A fierce, pure open-stock red that keeps its chroma through heavy blending.',4.00,78,'https://images.unsplash.com/photo-1504333638930-c8787321eee0?auto=format&fit=crop&w=1200&q=80'),
    ('p-072','cat-pastels','pastel-fixative-spray','Pastel Fixative Spray','A workable matte fixative that locks layers without dulling colour or filling the tooth.',18.00,26,'https://images.unsplash.com/photo-1607734834519-d8576ae60ea6?auto=format&fit=crop&w=1200&q=80'),
    ('p-073','cat-pastels','pastel-storage-box-foam','Pastel Storage Box, Foam','A shallow wooden box with grooved foam that cradles sticks and keeps colours from muddying.',47.00,12,'https://images.unsplash.com/photo-1493106641515-6b5631de4bb9?auto=format&fit=crop&w=1200&q=80'),
    ('p-074','cat-pastels','glassine-interleaving-sheets','Glassine Interleaving Sheets','Fifty smooth glassine sheets that protect finished pastels from smudging in transit.',11.00,40,'https://images.unsplash.com/photo-1579965342575-16428a7c8881?auto=format&fit=crop&w=1200&q=80'),
    ('p-075','cat-pastels','pastel-sanded-board-9x12','Pastel Sanded Board, 9x12','A fine grit-coated board that bites and holds an extraordinary number of pastel layers.',16.00,28,'https://images.unsplash.com/photo-1504333638930-c8787321eee0?auto=format&fit=crop&w=1200&q=80'),
    -- ----- Easels & Studio -----
    ('p-076','cat-easels','beechwood-studio-h-frame-easel','Beechwood Studio H-Frame Easel','A heavy, rock-steady H-frame in oiled beech that holds a six-foot canvas without a tremor.',329.00,6,'https://images.unsplash.com/photo-1452860606245-08befc0ff44b?auto=format&fit=crop&w=1200&q=80'),
    ('p-077','cat-easels','aluminium-field-easel','Aluminium Field Easel','A featherweight tripod easel that folds to a shoulder strap for painting wherever the light is.',96.00,14,'https://images.unsplash.com/photo-1578926375605-eaf7559b1458?auto=format&fit=crop&w=1200&q=80'),
    ('p-078','cat-easels','tabletop-h-frame-easel','Tabletop H-Frame Easel','A compact desk easel with the stability of a studio frame for small to mid-size work.',64.00,18,'https://images.unsplash.com/photo-1579783902614-a3fb3927b6a5?auto=format&fit=crop&w=1200&q=80'),
    ('p-079','cat-easels','pochade-box-9x12','Pochade Box, 9x12','A hardwood plein-air box that opens into easel, palette, and wet-panel carrier in one.',168.00,10,'https://images.unsplash.com/photo-1541961017774-22349e4a1262?auto=format&fit=crop&w=1200&q=80'),
    ('p-080','cat-easels','a-frame-lyre-easel','A-Frame Lyre Easel','A classic folding lyre easel in beech - gallery-ready display by day, painting by night.',78.00,13,'https://images.unsplash.com/photo-1452860606245-08befc0ff44b?auto=format&fit=crop&w=1200&q=80'),
    ('p-081','cat-easels','convertible-studio-easel','Convertible Studio Easel','Tilts from upright to flat for watercolour and back, with a crank-geared canvas tray.',412.00,4,'https://images.unsplash.com/photo-1578926375605-eaf7559b1458?auto=format&fit=crop&w=1200&q=80'),
    ('p-082','cat-easels','sketching-easel-travel','Sketching Easel, Travel','A telescoping field sketch easel that locks at any angle and weighs almost nothing.',52.00,20,'https://images.unsplash.com/photo-1579783902614-a3fb3927b6a5?auto=format&fit=crop&w=1200&q=80'),
    ('p-083','cat-easels','wooden-hand-palette-oval','Wooden Hand Palette, Oval','A balanced, oiled-walnut thumb-hole palette that seasons to a smooth mixing surface.',27.00,24,'https://images.unsplash.com/photo-1541961017774-22349e4a1262?auto=format&fit=crop&w=1200&q=80'),
    ('p-084','cat-easels','tear-off-paper-palette-pad','Tear-Off Paper Palette Pad','Fifty coated, oil-proof sheets - mix, paint, peel, and the cleanup is already done.',14.00,38,'https://images.unsplash.com/photo-1452860606245-08befc0ff44b?auto=format&fit=crop&w=1200&q=80'),
    ('p-085','cat-easels','glass-mixing-palette','Glass Mixing Palette','A tempered grey-glass slab that scrapes spotless and never stains - the last palette you''ll buy.',38.00,16,'https://images.unsplash.com/photo-1578926375605-eaf7559b1458?auto=format&fit=crop&w=1200&q=80'),
    ('p-086','cat-easels','brush-washer-sealed','Brush Washer, Sealed','A leak-proof steel washer with a coil screen that cleans bristles and seals solvent fumes away.',33.00,19,'https://images.unsplash.com/photo-1579783902614-a3fb3927b6a5?auto=format&fit=crop&w=1200&q=80'),
    ('p-087','cat-easels','studio-taboret-cart','Studio Taboret Cart','A rolling hardwood taboret with shallow tool drawers and a wipe-clean top for the working easel.',248.00,5,'https://images.unsplash.com/photo-1541961017774-22349e4a1262?auto=format&fit=crop&w=1200&q=80'),
    ('p-088','cat-easels','mahl-stick-adjustable','Mahl Stick, Adjustable','A telescoping mahl stick with a chamois pad that steadies the hand over wet passages.',24.00,22,'https://images.unsplash.com/photo-1452860606245-08befc0ff44b?auto=format&fit=crop&w=1200&q=80'),
    ('p-089','cat-easels','daylight-studio-lamp','Daylight Studio Lamp','A clamp lamp with a true 5000K daylight tube so colours read the same at midnight as at noon.',89.00,12,'https://images.unsplash.com/photo-1578926375605-eaf7559b1458?auto=format&fit=crop&w=1200&q=80'),
    ('p-090','cat-easels','canvas-carrier-straps-pair','Canvas Carrier Straps, Pair','Padded webbing straps that lock two wet canvases face-out for safe transport home.',21.00,30,'https://images.unsplash.com/photo-1579783902614-a3fb3927b6a5?auto=format&fit=crop&w=1200&q=80');
